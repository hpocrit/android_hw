package ru.kpfu.itis.android_hw

import java.util.Random

object NewsRepository {
    private val allNews: MutableList<NewsModel> = mutableListOf()
    private var news: MutableList<NewsModel> = mutableListOf()
    private val list = PhotoRepository.list
    private val onlyNews: MutableList<NewsModel.News> = mutableListOf()

    init {
        generateNews()
    }

    fun generateNews() {

        allNews.add(NewsModel.NewsButton())


        for (i in 1..60) {
            val title = "Заголовок новости $i"
            val content = "Содержание новости $i"
            val newsItem = NewsModel.News(i, title, content, list[(Math.random()*10).toInt()],false)
            allNews.add(newsItem)
            onlyNews.add(newsItem)
        }
        news = allNews
        addDateItems()
    }

    fun getNews(n : Int) : MutableList<NewsModel> {
        news = allNews.subList(0, n)
        return news
    }

    fun getAllNews() : MutableList<NewsModel> = news

    private fun addDateItems() {
        var i = 1
        while (i < news.size) {
            news.add(i, NewsModel.NewsDate())
            i += 9
        }
    }


    fun addRandomItems(cnt: Int): MutableList<NewsModel> {
        val random = Random()
        for (i in 1..cnt) {
            news.add(
                random.nextInt(news.size - 1) + 1,
                onlyNews[news.size - 2 + i]
            )
        }
        return news
    }

    fun delete(id: Int) : Int{
        for (i in 0..news.size) {
            if((news[i] as? NewsModel.News)?.newsId == id){
                news.removeAt(i)
                return i
            }
        }
        return -1
    }




}