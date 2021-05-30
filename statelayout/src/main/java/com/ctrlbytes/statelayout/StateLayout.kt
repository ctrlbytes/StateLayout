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

    fun showEmpty(message: String) {
        state = State.EMPTY
        contentView.isVisible = false
        progressBarContainer.isVisible = false
        errorContainer.isVisible = true

        tvErrorMessage.text = message
        ivErrorIcon.setImageResource(emptyImage)
    }

    fun showEmpty(message: Int) = showError(context.getString(message))

    fun onButtonClick(block: () -> Unit) {
        btnRetry.setOnClickListener { block() }
    }

    fun setOnStateChangeListener(block: (State) -> Unit) {
        onStateChangeListener = block
    }
}