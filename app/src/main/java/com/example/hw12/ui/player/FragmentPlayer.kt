package com.example.hw12.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hw12.R
import com.example.hw12.databinding.FragmentPlayerBinding
import com.example.hw12.databinding.FragmentPlayerBindingImpl
import com.example.hw12.ui.NetflixViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class FragmentPlayer : Fragment(R.layout.fragment_player) {
    private val args by navArgs<FragmentPlayerArgs>()
    private val mainModel by activityViewModels<NetflixViewModel>()
    private val model by viewModels<ViewModelPlayer>()
    lateinit var binding: FragmentPlayerBinding
    lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init() {
        val source = "https://aspb36.cdn.asset.aparat.com/aparat-video/7de46dd2e4061ca8011a563ae1f7272537535010-1080p.mp4?wmsAuthSign=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6IjVlZjI3MjI5NTA1YjQzZjFiMzQ2NDQ2MDI1MjFiYWJhIiwiZXhwIjoxNjQ2NjczMDYxLCJpc3MiOiJTYWJhIElkZWEgR1NJRyJ9.EPxnC_XZ8heYJ_2XbBnamR-SAd5DSI1Kla397S4BbVc"
        val context = requireContext()
        player = ExoPlayer.Builder(context).build().apply {
            with(binding) {
                playerView.player = this@apply
            }
            addMediaItem(createMediaItem(source))
        }
        player.prepare()
        player.play()
        player.seekTo(model.currentPosition)
        mainModel.hide()
    }

    private fun createMediaItem(uri: String): MediaItem {
        return MediaItem.fromUri(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        model.currentPosition = player.currentPosition
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        mainModel.hide.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}