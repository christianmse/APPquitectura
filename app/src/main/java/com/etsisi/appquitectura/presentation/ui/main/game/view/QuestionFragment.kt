package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.common.QuestionListener
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.AnswersAdapter
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName
import java.util.concurrent.TimeUnit

class QuestionFragment(
    private val gameListener: GameListener?,
    private val questionBO: QuestionBO
) : BaseFragment<FragmentQuestionBinding, EmptyViewModel>(
    R.layout.fragment_question,
    EmptyViewModel::class
), QuestionListener {
    private var counterMillisUntilFinished = 0L
    private var counter: CountDownTimer? = null

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
        private const val COUNT_DOWN_MILLIS = 10000L
        private const val THREE_SECONDS = 3000L
        @JvmStatic
        fun newInstance(question: QuestionBO, listener: GameListener?) =
            QuestionFragment(listener, question)
    }

    override fun setUpDataBinding(mBinding: FragmentQuestionBinding, mViewModel: EmptyViewModel) {
        mBinding.apply {
            Log.e("XXX", "setUpDataBinding for question ${question?.title}")
            question = questionBO
            answersRecyclerView.adapter = AnswersAdapter(this@QuestionFragment, questionBO).also {
                it.addDataSet(questionBO.answers.asSequence().shuffled().toList())
            }
            imageQuestion.apply {
                Glide.with(this)
                    .load(questionBO.getImageFirestorageReference())
                    .centerInside()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Log.e(TAG, "${getMethodName(object {}.javaClass)} $e")
                            setImageResource(R.drawable.etsam)
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(this)
            }
            counter = object : CountDownTimer(COUNT_DOWN_MILLIS, COUNT_DOWN_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("XXX", "onTick for questionId ${question?.id}")
                    counterMillisUntilFinished = millisUntilFinished
                    progressText.text = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
                    progressBar.progress -= 1
                    if (millisUntilFinished <= THREE_SECONDS) {
                        progressText.setTextColor(Color.RED)
                    }
                }

                override fun onFinish() {
                    progressBar.progress = 0
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        counter?.start()
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
    }

    override fun onAnswerClicked(question: QuestionBO, answer: AnswerBO) {
        counter?.cancel()
        gameListener?.onAnswerClicked(question, answer, counterMillisUntilFinished)
    }
}