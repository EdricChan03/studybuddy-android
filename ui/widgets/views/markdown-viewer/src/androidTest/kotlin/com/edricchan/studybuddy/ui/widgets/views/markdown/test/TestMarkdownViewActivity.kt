package com.edricchan.studybuddy.ui.widgets.views.markdown.test

import androidx.activity.ComponentActivity
import com.edricchan.studybuddy.ui.widgets.views.markdown.MarkdownView

class TestMarkdownViewActivity : ComponentActivity(R.layout.markdown_viewer_test) {
    val markdownView: MarkdownView get() = findViewById(R.id.markdown_view)
}
