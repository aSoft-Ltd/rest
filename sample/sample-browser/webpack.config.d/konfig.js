config.resolve.modules.push("/media/andylamax/63C23C360914D355/aSoft/CSS Libs/rest/code/sample/sample-browser/build/resources/main")
config.module.rules.push({
    test: /\.(png|jpe?g|gif|svg)$/i,
    use: [
      {
        loader: 'file-loader',
      },
    ],
});