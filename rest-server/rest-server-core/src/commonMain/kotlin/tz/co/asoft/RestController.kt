package tz.co.asoft

open class RestController<T : Entity>(private val dao: IDao<T>) : IRestController<T>, IDao<T> by dao