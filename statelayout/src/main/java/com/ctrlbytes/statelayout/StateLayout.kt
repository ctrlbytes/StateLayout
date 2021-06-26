/*
 * MIT License
 *
 * Copyright (c) 2021 CtrlBytes Technologies
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ctrlbytes.statelayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible

@Suppress("MemberVisibilityCanBePrivate", "unused")
class StateLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var state: State = State.CONTENT
        set(newState) {
            onStateChangeListener?.let {
                it(newState)
            }
            field = newState
        }

    private var onStateChangeListener: ((State) -> Unit)? = null

    private val buttonText: Int
    private val emptyImage: Int
    private val errorImage: Int
    private val loadingViewId: Int

    private lateinit var contentView: View
    private lateinit var progressBarContainer: ViewGroup
    private lateinit var errorContainer: ViewGroup
    private lateinit var ivErrorIcon: ImageView
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnRetry: Button

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0).apply {
            try {
                buttonText = getResourceId(
                    R.styleable.StateLayout_buttonText,
                    R.string.statelayout_default_buttonText
                )
                emptyImage = getResourceId(R.styleable.StateLayout_emptyImage, R.drawable.ic_ufo)
                errorImage = getResourceId(R.styleable.StateLayout_errorImage, R.drawable.ic_ufo)
                loadingViewId = getResourceId(R.styleable.StateLayout_loadingView, NO_ID)
            } finally {
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setup()
    }

    private fun setup() {
        if (childCount == 0) throw Exception("No Child Present!")
        contentView = getChildAt(0)
        val inflatedView = View.inflate(context, R.layout.view_statelayout, null)
        progressBarContainer = inflatedView.findViewById(R.id.progress_bar_container)
        errorContainer = inflatedView.findViewById(R.id.error_container)
        ivErrorIcon = inflatedView.findViewById(R.id.iv_error_icon)
        tvErrorMessage = inflatedView.findViewById(R.id.tv_error_message)
        btnRetry = inflatedView.findViewById(R.id.btn_retry)

        btnRetry.setText(buttonText)

        if (loadingViewId != NO_ID) {
            val view = inflate(context, loadingViewId, null)
            progressBarContainer.removeAllViews()
            progressBarContainer.addView(view)
        }

        addView(inflatedView)
    }

    fun showContent() {
        state = State.CONTENT
        contentView.isVisible = true
        progressBarContainer.isVisible = false
        errorContainer.isVisible = false
    }

    fun showLoading() {
        state = State.LOADING
        contentView.isVisible = false
        progressBarContainer.isVisible = true
        errorContainer.isVisible = false
    }

    fun showError(message: String) {
        state = State.ERROR
        contentView.isVisible = false
        progressBarContainer.isVisible = false
        errorContainer.isVisible = true

        tvErrorMessage.text = message
        ivErrorIcon.setImageResource(errorImage)
    }

    fun showError(message: Int) = showError(context.getString(message))

    fun showEmpty(message: String, showRetry: Boolean = true) {
        state = State.EMPTY
        contentView.isVisible = false
        progressBarContainer.isVisible = false
        errorContainer.isVisible = true
        btnRetry.isVisible = showRetry

        tvErrorMessage.text = message
        ivErrorIcon.setImageResource(emptyImage)
    }

    fun showEmpty(message: Int, showRetry: Boolean = true) = showEmpty(context.getString(message), showRetry)

    fun onButtonClick(block: () -> Unit) {
        btnRetry.setOnClickListener { block() }
    }

    fun setOnStateChangeListener(block: (State) -> Unit) {
        onStateChangeListener = block
    }
}