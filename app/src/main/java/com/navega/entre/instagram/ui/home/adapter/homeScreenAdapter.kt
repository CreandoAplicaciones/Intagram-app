package com.navega.entre.instagram.ui.home.adapter

import android.content.Context
import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.navega.entre.instagram.R
import com.navega.entre.instagram.core.BaseViewHolder
import com.navega.entre.instagram.core.TimeUtils
import com.navega.entre.instagram.data.model.post
import com.navega.entre.instagram.databinding.PostItemViewBinding
import java.sql.Time

class homeScreenAdapter(
    private val postList: List<post>,
    private val onPostClickListener: OnPostClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var postClickListener: OnPostClickListener? = null

    //inflater the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBiding =
            PostItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeScreenViewHolder(itemBiding, parent.context)
    }

    //put the information of down to the post
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }

    }

    //return the number of items
    override fun getItemCount(): Int = postList.size


    private inner class HomeScreenViewHolder(
        val binding: PostItemViewBinding,
        val context: Context
    ) : BaseViewHolder<post>(binding.root) {

        override fun bind(items: post) {

            setupProfileInfo(items)
            addPostTimeStamp(items)
            setupPostImage(items)
            setPostDescription(items)
            tintHeartIcon(items)
            setupLikeCount(items)
            setLikeClickAction(items)

        }

        private fun setupProfileInfo(post: post) {
            Glide.with(context).load(post.poster?.profile_picture).centerCrop()
                .into(binding.profilePicture)
            binding.profileName.text = post.poster?.username
        }

        private fun addPostTimeStamp(post: post) {
            val createdAt = (post.created_at?.time?.div(1000L))?.let {
                TimeUtils.getTimeAgo(it.toInt())
            }

            binding.postTimestamp.text = createdAt
        }

        private fun setupPostImage(post: post) {
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImagen)

        }

        private fun setPostDescription(post: post) {
            if (post.post_description.isEmpty()) {
                binding.postDescription.visibility = View.GONE
            } else {
                binding.postDescription.text = post.post_description
            }

        }

        private fun tintHeartIcon(post: post) {
            if (!post.liked) {
                binding.likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_favorite_border_24
                    )
                )
                binding.likeBtn.setColorFilter(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_favorite_24
                    )
                )
                binding.likeBtn.setColorFilter(ContextCompat.getColor(context, R.color.red))

            }
        }

        private fun setupLikeCount(post: post) {
            if (post.likes > 0) {
                binding.likeTxt.visibility = View.VISIBLE
                binding.likeTxt.text = "${post.likes} likes"
            } else {
                binding.likeTxt.visibility = View.INVISIBLE

            }
        }

        private fun setLikeClickAction(post: post) {
        binding.likeBtn.setOnClickListener{
            if (post.liked)post.apply {
                liked=false
            }else {post.apply { liked=true }
            tintHeartIcon(post)
                postClickListener?.onLikeButtonClick(post,post.liked)
            }
        }
        }

    }

    interface OnPostClickListener {
        fun onLikeButtonClick(post: post, liked: Boolean)
    }


}