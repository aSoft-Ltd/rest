package tz.co.asoft

internal class RestfulPageLoader<D : Entity>(
    private val restfulDao: IRestfulDao<D>,
    override val predicate: (D) -> Boolean
) : PageLoader<VKey, D> {

    private suspend fun loadPage(pageSize: Int, at: VKey?): Pair<VKey, List<D>> {
        val key = at ?: VKey(0, null)
        val pageNo = key.pageNo
        val nodes = mutableListOf<D>()
        val allNodes: List<D> = restfulDao.load(at?.uid, pageSize)
        val startIndex = if (at?.uid == null) 0 else allNodes.indexOfFirst { it.uid == at.uid }
        val unfilteredNodes = allNodes.subList(startIndex, allNodes.size)

        val lastDoc = unfilteredNodes.lastOrNull() ?: return key to nodes
        val filteredSnaps = (unfilteredNodes - lastDoc).filter(predicate)
        nodes += filteredSnaps
        if (nodes.size >= pageSize) {
            return VKey(key.pageNo, lastDoc.uid) to nodes + listOf(lastDoc).filter(predicate)
        }

        if (unfilteredNodes.size < pageSize) {
            return VKey(key.pageNo, lastDoc.uid) to nodes + listOf(lastDoc).filter(predicate)
        }

        val pair = loadPage(
            pageSize - nodes.size,
            VKey(pageNo + 1, null)
        )
        return pair.first to nodes + pair.second
    }

    override suspend fun prevOf(node: Page<VKey, D>): Page<VKey, D> {
        val key = node.prev?.key ?: throw Exception("Can't go prev with null key")
        val data = loadPage(node.pageSize, key)
        val nextKey = if (data.second.size < (node.pageSize + 1)) null else data.first
        return Page(
            data = if (data.second.size < node.pageSize) data.second else data.second.dropLast(1),
            key = key,
            prev = node.prev?.prev,
            next = node,
            nextKey = nextKey,
            pageSize = node.pageSize
        )
    }

    override suspend fun nextOf(node: Page<VKey, D>): Page<VKey, D> {
        val key = node.nextKey ?: throw Exception("Can't go next with null key")
        val data = loadPage(node.pageSize, key)
        val nextKey = if (data.second.size < (node.pageSize + 1)) null else data.first
        return Page(
            data = if (data.second.size < node.pageSize) data.second else data.second.dropLast(1),
            key = key,
            prev = node,
            next = null,
            nextKey = nextKey,
            pageSize = node.pageSize
        )
    }

    override suspend fun firstPage(pageSize: Int): Page<VKey, D> {
        val data = loadPage(pageSize, null)
        val key = VKey(0, null)
        val nextKey = if (data.second.size < (pageSize + 1)) null else data.first
        return Page(
            data = if (data.second.size < pageSize) data.second else data.second.dropLast(1),
            key = key,
            prev = null,
            next = null,
            nextKey = nextKey,
            pageSize = pageSize
        )
    }
}