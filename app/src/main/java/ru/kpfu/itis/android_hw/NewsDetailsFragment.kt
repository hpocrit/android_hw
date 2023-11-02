package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android_hw.databinding.FragmentNewsDetailsBinding

class NewsDetailsFragment : Fragment(R.layout.fragment_news_details) {
    private var binding: FragmentNewsDetailsBinding? = null
    private val NAME_KEY = "key"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsDetailsBinding.bind(view)

        binding?.run {
            val index = arguments?.getInt(ARG_INDEX)!!
            val name = arguments?.getString(ARG_NAME)!!
            val item = NewsRepository.getAllNews().single { (it as? NewsModel.News)?.newsId == index } as NewsModel.News
            title.text = item.newsTitle

            newsDetailsTv.text = item.newsDetails
            item.newsDetails?.let { newsDetailsTv.text = it }
            item.newsImage?.let { res ->
                newsImageIv.setImageResource(res)
            }
            newsImageIv.transitionName = name
        }


    }

    fun getBundle(transitionName: String?): Bundle? {
        val args = Bundle()
        args.putString(NAME_KEY, transitionName)
        return args
    }

    companion object{

        private const val ARG_INDEX = "index"
        private const val ARG_NAME = "i"

        fun newInstance(text: Int, transitionName: String) = NewsDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_INDEX, text)
                putString(ARG_NAME, transitionName)
            }
        }
    }
}