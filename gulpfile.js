const gulp = require('gulp');
const conventionalChangelog = require('gulp-conventional-changelog');

gulp.task('changelog', () => {
	return gulp.src('CHANGELOG.md')
		.pipe(conventionalChangelog({
			preset: 'angular',
			// Regenerate everything
			// releaseCount: 0
		}))
		.pipe(gulp.dest('./'));
})
