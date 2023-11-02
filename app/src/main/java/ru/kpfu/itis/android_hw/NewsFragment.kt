package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android_hw.databinding.FragmentNewsBinding

class NewsFragment : Fragment(R.layout.fragment_news) {
    private var binding: FragmentNewsBinding? = null
    private var newsAdapter: NewsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)

        binding?.run {
            val cnt = arguments?.getInt(ARG_CNT)!!
            newsAdapter = NewsAdapter(
                //onNewsClicked = ::onNewsClicked,
                onLikeClicked = ::onLikeClicked,
                cnt = cnt + (cnt/8) + 2,
                fragmentManager = parentFragmentManager,
                onDeleteClicked = ::onDeleteClicked,
                activity = requireActivity()
            )
            newsRv.adapter = newsAdapter

            val columnCnt = if (cnt <= 12) 1 else 2

            val layoutManager = GridLayoutManager(
                requireContext(),
                columnCnt,
                GridLayoutManager.VERTICAL,
                false
            )

            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    if (columnCnt == 1 || NewsRepository.getAllNews()[position] is NewsModel.News) 1
                    else 2
            }
            newsRv.layoutManager = layoutManager
            if(cnt <= 12) {

                val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    val list = NewsRepository.getAllNews()

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val item = list[position]

                        if (item is NewsModel.News) {
                            list.removeAt(position)
                            newsAdapter?.notifyItemRemoved(position)
                            val snackbar = Snackbar.make(binding!!.newsRv, "Элемент удален", Snackbar.LENGTH_LONG)

                            snackbar.setAction("Отменить") {
                                NewsRepository.getAllNews().add(position, item)
                                newsAdapter?.notifyItemInserted(position)
                            }
                            snackbar.show()
                        }
                    }
                    override fun getSwipeDirs(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder
                    ): Int {
                        val position = viewHolder.adapterPosition
                        val item = list.getOrNull(position)

                        if (item is NewsModel.News) {
                            return if (position != RecyclerView.NO_POSITION) {
                                ItemTouchHelper.LEFT
                            } else {
                                super.getSwipeDirs(recyclerView, viewHolder)
                            }
                        }

                        return 0
                    }
                }
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(newsRv)
            }
        }
    }
    companion object{
        private const val ARG_CNT = "cnt_arg"

        fun newInstance(text: Int) = NewsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CNT, text)
            }
        }
    }


    private fun onLikeClicked(position: Int, news: NewsModel.News) {
        newsAdapter?.updateItem(position, news)
    }

    private fun onDeleteClicked(news: NewsModel.News) {
        val position = NewsRepository.delete(news.newsId)
        newsAdapter?.notifyItemRemoved(position)
        val snackbar = Snackbar.make(binding!!.newsRv, "Элемент удален", Snackbar.LENGTH_LONG)

        snackbar.setAction("Отменить") {
            NewsRepository.getAllNews().add(position, news)
            newsAdapter?.notifyItemInserted(position)
        }
        snackbar.show()
    }
}
