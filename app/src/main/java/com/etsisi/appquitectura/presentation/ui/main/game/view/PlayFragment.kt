package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentPlayBinding
import com.etsisi.appquitectura.databinding.ItemTabHeaderBinding
import com.etsisi.appquitectura.domain.enums.GameNavType
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.common.PlayFragmentListener
import com.etsisi.appquitectura.presentation.components.ZoomOutPageTransformer
import com.etsisi.appquitectura.presentation.ui.main.adapter.QuestionsViewPagerAdapter
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.game.viewmodel.PlayViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.concurrent.TimeUnit

class PlayFragment : BaseFragment<FragmentPlayBinding, PlayViewModel>(
    R.layout.fragment_play,
    PlayViewModel::class
), PlayFragmentListener, TabLayout.OnTabSelectedListener, GameListener {

    val args: PlayFragmentArgs by navArgs()
    private val questionsAdapter by lazy { QuestionsViewPagerAdapter(this) }
    private val viewPager: ViewPager2
        get() = mBinding.viewPager
    private val readySetGoCounter by lazy {
        object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                mViewModel.setNavType(GameNavType.START_GAME)
            }
        }
    }
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private val nextQuestionRunnable = Runnable {
        if (viewPager.currentItem < viewPager.adapter?.itemCount?.minus(1) ?: 0) {
            viewPager.currentItem++
        } else {
            Toast.makeText(context, "${mViewModel._userGameResult.getAllCorrectAnswers()} correctas", Toast.LENGTH_SHORT).show()
            handler = null
        }
    }

    companion object {
        private const val SELECTED_ALPHA = 1.0F
        private const val UNSELECTED_ALPHA = 0.2F
    }

    override fun setUpDataBinding(mBinding: FragmentPlayBinding, mViewModel: PlayViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            viewModel = mViewModel
            mViewModel.setNavType(args.navType)
            if (args.navType == GameNavType.PRE_START_GAME) {
                readySetGoCounter.start().also {
                    readyToStartGame.playAnimation()
                }
            }
            listener = this@PlayFragment
            this@PlayFragment.viewPager.apply {
                adapter = questionsAdapter
                isUserInputEnabled = false
                setPageTransformer(ZoomOutPageTransformer())
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.customView = ItemTabHeaderBinding.inflate(layoutInflater, tab.view, false)
                    .apply {
                        questionPosition.text = position.toString()
                    }.root
                setTabAlpha(tab, false)
            }.attach()
            tabLayout.apply {
                setSelectedTabIndicator(null)
                addOnTabSelectedListener(this@PlayFragment)
            }
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: PlayViewModel) {
        with(mViewModel) {
            navType.observe(viewLifecycleOwner) {
                if (it == GameNavType.PRE_START_GAME) {
                    fetchInitialQuestions(args.gameMode)
                }
            }
            questions.observe(viewLifecycleOwner) {
                questionsAdapter.addData(it, mBinding.tabLayout.selectedTabPosition)
            }
        }
    }

    override fun onGameMode(item: ItemGameMode) {
        navigator.startGame(item.action)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    override fun onTabUnselected(tab: TabLayout.Tab?) = setTabAlpha(tab, false)

    override fun onTabReselected(tab: TabLayout.Tab?) = setTabAlpha(tab, true)

    private fun setTabAlpha(tab: TabLayout.Tab?, selected: Boolean) {
        tab?.customView?.alpha = if (selected) SELECTED_ALPHA else UNSELECTED_ALPHA
    }

    override fun onAnswerClicked(question: QuestionBO, answer: AnswerBO, userMarkInMillis: Long) {
        mViewModel.setGameResultAccumulated(question, answer, userMarkInMillis)
        handler?.postDelayed(nextQuestionRunnable, 500L)
    }

    override fun onCounterTimeOut(question: QuestionBO) {
        mViewModel.setGameResultAccumulated(question, null, 0)
        handler?.postDelayed(nextQuestionRunnable, 500L)
    }

}