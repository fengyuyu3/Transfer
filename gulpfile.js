var gulp = require('gulp'),
    runSequence = require('run-sequence'),
    sass = require('gulp-ruby-sass'),
    autoprefixer = require('gulp-autoprefixer'),
    uglify = require('gulp-uglify'),
    imagemin = require('gulp-imagemin'),
    clean = require('gulp-clean'),
    notify = require('gulp-notify'),
    cache = require('gulp-cache'),
    connect = require('gulp-connect'),
    replace = require('gulp-replace'),
    htmlmin = require('gulp-htmlmin'),
    livereload = require('gulp-livereload'),
    stripDebug = require('gulp-strip-debug'),
    rev = require('gulp-rev'),
    //- 对文件名加MD5后缀
    revCollector = require('gulp-rev-collector'),
    less = require('gulp-less'),
    concat = require('gulp-concat'),
    rename = require('gulp-rename'),
    plumber = require('gulp-plumber'),
    minifycss = require('gulp-minify-css');

//-jshint = require('gulp-jshint')
// 合并、压缩、重命名css
// 正式版
gulp.task('css', function() {
    gulp.src(['app/css/*.css'])
        .pipe(rev())
        .pipe(minifycss()) // 压缩css文件
        .pipe(gulp.dest('dist/css/')) // 输出
        .pipe(rev.manifest())
        .pipe(gulp.dest('dist/rev/css/'));
});
// 开发版
gulp.task('testCss', function() {
    gulp.src(['app/css/*.css'])
        .pipe(minifycss()) // 压缩css文件
        .pipe(gulp.dest('dist/css/')) // 输出
});
// 正式版的脚本
gulp.task('scripts', function() {
    return gulp.src('app/js/**/*.js')
        //清除console
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(rev())
        .pipe(gulp.dest('dist/js/'))
        .pipe(rev.manifest())
        .pipe(gulp.dest('dist/rev/js/'));

});
// 开发版的脚本
gulp.task('testScripts', function() {
    return gulp.src('app/js/**/*.js')
        //清除console
        .pipe(gulp.dest('dist/js/'));
});

// 文件名后缀
gulp.task('rev', function() {
    gulp.src(['dist/rev/**/*.json', 'dist/view/**']) //- 读取 rev-manifest.json 文件以及需要进行css名替换的文件
        .pipe(revCollector()) //- 执行文件内后缀名的替换
        .pipe(gulp.dest('dist/view/')); //- 替换后的文件输出的目录
});
gulp.task('revJs', function() {
    gulp.src(['dist/rev/**/*.json', 'dist/js/**/*.js']) //- 读取 rev-manifest.json 文件以及需要进行css名替换的文件
        .pipe(revCollector()) //- 执行文件内后缀名的替换
        .pipe(gulp.dest('dist/js/')); //- 替换后的文件输出的目录
});
// 图片
gulp.task('images', function() {
    return gulp.src('app/image/**/**')
        .pipe(cache(imagemin({
            optimizationLevel: 3,
            progressive: true,
            interlaced: true
        })))
        .pipe(gulp.dest('dist/image/'));
});
//插件
gulp.task('Plug', function() {
    return gulp.src('app/plug/**')
        .pipe(gulp.dest('dist/Plug/'));
});
//资源
gulp.task('resource', function() {
        gulp.src('app/font/**')
        .pipe(gulp.dest('dist/font/'));
        gulp.src('app/data/**')
        .pipe(gulp.dest('dist/data/'));
        return ;
});
//html
gulp.task('views', function() {
    gulp.src(["app/view/*.html", "!app/view/index.html", "!app/view/login.html", "!app/view/infoArticleDetail.html","!app/view/download.html"])
        .pipe(htmlmin({
            removeComments: true, //清除HTML注释
            collapseWhitespace: true, //压缩HTML
            collapseBooleanAttributes: true, //省略布尔属性的值 <input checked="true"/> ==> <input />
            removeEmptyAttributes: true, //删除所有空格作属性值 <input id="" /> ==> <input />
            removeScriptTypeAttributes: true, //删除<script>的type="text/javascript"
            removeStyleLinkTypeAttributes: true, //删除<style>和<link>的type="text/css"
            minifyJS: true, //压缩页面JS
            minifyCSS: true //压缩页面CSS
        }))
        //.pipe(rev())
        .pipe(gulp.dest("dist/view/"));
        //.pipe(rev.manifest())
        //.pipe(gulp.dest('dist/rev/view/'));

    return gulp.src(["app/view/index.html", "app/view/login.html", "app/view/infoArticleDetail.html","app/view/download.html"])
        .pipe(gulp.dest("dist/view/"));
});
//iconfont
gulp.task('iconfont', function() {
    return gulp.src('app/css/iconfont/**')
        .pipe(gulp.dest('dist/css/iconfont/'));
});

// 清理
gulp.task('clean', function() {
    return gulp.src(['dist/*'], {
            read: false
        })
        .pipe(clean());
});

// 预设任务
gulp.task('default', ['clean'], function() {
    gulp.start('scripts', 'images', 'views');
});
// 看守
gulp.task('watch', function() {
    // 看守所有.js档
    gulp.watch('app/js/**/*.js', ['testScripts']);
    // 看守所有图片档
    gulp.watch('app/image/**/*', ['images']);
    // 看守所有的页面
    gulp.watch('app/view/*.html', ['views']);
    // 看守所有的页面
    gulp.watch('app/css/*.css', ['testCss']);
    // 建立即时重整伺服器
    var distServer = livereload();

    // 看守所有位在 dist/  目录下的档案，一旦有更动，便进行重整
    gulp.watch(['dist/**']).on('change', function(file) {
        livereload.reload("dist/view/index.html");
    });
});

gulp.task('build', function(done) {
    runSequence(['clean'], ['css'], ['iconfont'], ['images'], ['scripts'], ['views'], ['Plug'], ['resource'], ['rev'], ['revJs'], done);
});
gulp.task('testBuild', function(done) {
    runSequence(['testCss'], ['iconfont'], ['images'], ['testScripts'], ['views'], ['Plug'], ['resource'], done);
});

// 正式版的脚本
gulp.task('baiduscripts', function() {
    return gulp.src('app/Plug/ueditor/ueditor.allbig.js')
        //清除console
        .pipe(stripDebug())
        .pipe(uglify())
        .pipe(gulp.dest('dist/js/'));

});
// 启动服务
gulp.task('server', ['testBuild', 'watch'], function() {
    connect.server({
        root: 'dist',
        prot: '8080',
        livereload: true
    });
});
