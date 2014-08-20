/* jshint node: true */
var gulp = require('gulp');
var plumber = require('gulp-plumber');
var less = require('gulp-less');

gulp.task('less', function() {
  return gulp.src('resources/public/css/main.less')
  .pipe(plumber())
  .pipe(less())
  .pipe(gulp.dest('resources/public/css'));
});

gulp.task('default', ['less'], function() {
  gulp.watch('resources/public/css/*.less', ['less']);
});
