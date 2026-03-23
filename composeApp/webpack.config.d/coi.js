// As per https://github.com/gzuidhof/coi-serviceworker?tab=readme-ov-file

var CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin(
        {
            patterns: [
                  "../../node_modules/coi-serviceworker/coi-serviceworker.js"
            ]
        }
    )
);