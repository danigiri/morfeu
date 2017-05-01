var browserSync = require('browser-sync').create();
var proxy = require('http-proxy-middleware');


var apiProxy = proxy('/morfeu', {
    target: 'http://localhost:8080/'
    ,changeOrigin: true   // for vhosted sites
    ,logLevel: "debug"
    
});

module.exports = {
    server: {
    	reloadDelay: 100
        ,baseDir: "./"
            ,routes: {
                "/morfeu": "../../test/resources"
            }
        ,middleware: [apiProxy],
    }
};