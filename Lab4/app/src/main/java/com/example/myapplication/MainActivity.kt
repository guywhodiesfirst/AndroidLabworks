package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnSelectFile: Button
    private lateinit var tvFileName: TextView

    private lateinit var audioLayout: LinearLayout
    private lateinit var videoLayout: LinearLayout

    private var isAudioPrepared = false
    private lateinit var btnPlayAudio: Button
    private lateinit var btnPauseAudio: Button
    private lateinit var btnStopAudio: Button
    private lateinit var audioSeekBar: SeekBar

    private lateinit var btnPlayVideo: Button
    private lateinit var btnPauseVideo: Button
    private lateinit var btnStopVideo: Button
    private lateinit var videoSeekBar: SeekBar
    private lateinit var videoView: VideoView

    private var mediaPlayer: MediaPlayer? = null
    private var audioHandler = Handler(Looper.getMainLooper())
    private var videoHandler = Handler(Looper.getMainLooper())

    private var selectedUri: Uri? = null

    companion object {
        const val REQUEST_CODE_SELECT_FILE = 1001
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelectFile = findViewById(R.id.btnSelectFile)
        tvFileName = findViewById(R.id.tvFileName)

        audioLayout = findViewById(R.id.audioLayout)
        videoLayout = findViewById(R.id.videoLayout)

        btnPlayAudio = findViewById(R.id.btnPlayAudio)
        btnPauseAudio = findViewById(R.id.btnPauseAudio)
        btnStopAudio = findViewById(R.id.btnStopAudio)
        audioSeekBar = findViewById(R.id.audioSeekBar)

        btnPlayVideo = findViewById(R.id.btnPlayVideo)
        btnPauseVideo = findViewById(R.id.btnPauseVideo)
        btnStopVideo = findViewById(R.id.btnStopVideo)
        videoSeekBar = findViewById(R.id.videoSeekBar)
        videoView = findViewById(R.id.videoView)

        btnSelectFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("audio/*", "video/*"))
            startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)

        }

        btnPlayAudio.setOnClickListener {
            if (isAudioPrepared) {
                mediaPlayer?.start()
                mediaPlayer?.let {
                    startUpdatingSeekBar(audioSeekBar, it)
                    updateAudioButtons()
                }
            }
        }

        btnPauseAudio.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                updateAudioButtons()
            }
        }

        btnStopAudio.setOnClickListener {
            audioHandler.removeCallbacksAndMessages(null)
            mediaPlayer?.let {
                if (it.isPlaying || isAudioPrepared) {
                    try {
                        it.stop()
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                    it.release()
                }
            }

            mediaPlayer = null
            isAudioPrepared = false
            audioSeekBar.progress = 0
            updateAudioButtons()

            selectedUri?.let { uri ->
                setupAudio(uri)
            }
        }

        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(sb: SeekBar?) {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
            }

            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStopTrackingTouch(sb: SeekBar?) {
                sb?.let {
                    if (isAudioPrepared) {
                        mediaPlayer?.seekTo(it.progress)
                        mediaPlayer?.start()
                        mediaPlayer?.let { player ->
                            startUpdatingSeekBar(audioSeekBar, player)
                        }
                    }
                }
            }
        })

        btnPlayVideo.setOnClickListener {
            videoView.start()
            startUpdatingSeekBar(videoSeekBar, videoView)
            updateVideoButtons()
        }

        btnPauseVideo.setOnClickListener {
            videoView.pause()
            updateVideoButtons()
        }

        btnStopVideo.setOnClickListener {
            videoHandler.removeCallbacksAndMessages(null)
            videoView.stopPlayback()
            videoSeekBar.progress = 0
            selectedUri?.let {
                videoView.setVideoURI(it)
                videoView.seekTo(1)
            }
            updateVideoButtons()
        }

        videoSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                videoView.pause()
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                videoView.start()

                startUpdatingSeekBar(videoSeekBar, videoView)
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            selectedUri = uri
            val fileName = uri.path?.split("/")?.last() ?: "Файл"
            tvFileName.text = fileName

            val mimeType = contentResolver.getType(uri)

            if (mimeType?.startsWith("audio") == true) {
                setupAudio(uri)
            } else if (mimeType?.startsWith("video") == true) {
                setupVideo(uri)
            } else {
                Toast.makeText(this, "Непідтримуваний формат", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAudio(uri: Uri) {
        videoLayout.visibility = LinearLayout.GONE
        audioLayout.visibility = LinearLayout.VISIBLE

        mediaPlayer?.release()
        isAudioPrepared = false

        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@MainActivity, uri)
            setOnPreparedListener {
                audioSeekBar.max = duration
                isAudioPrepared = true
                updateAudioButtons()
            }
            prepareAsync()
        }
    }

    private fun setupVideo(uri: Uri) {
        audioLayout.visibility = LinearLayout.GONE
        videoLayout.visibility = LinearLayout.VISIBLE

        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp ->
            videoSeekBar.max = videoView.duration
            updateVideoButtons()
            startUpdatingSeekBar(videoSeekBar, videoView)

            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight

            if (videoWidth > 0 && videoHeight > 0) {
                val screenWidth = resources.displayMetrics.widthPixels
                val newHeight = (screenWidth.toFloat() * videoHeight / videoWidth).toInt()

                val layoutParams = videoView.layoutParams
                layoutParams.width = screenWidth
                layoutParams.height = newHeight
                videoView.layoutParams = layoutParams
            }
        }
        videoView.seekTo(1)
    }

    private fun startUpdatingSeekBar(seekBar: SeekBar, player: Any) {
        val handler = if (player is MediaPlayer) audioHandler else videoHandler

        handler.removeCallbacksAndMessages(null)

        val updateTask = object : Runnable {
            override fun run() {
                if (player is MediaPlayer && player.isPlaying) {
                    seekBar.progress = player.currentPosition
                    handler.postDelayed(this, 500)
                } else if (player is VideoView && player.isPlaying) {
                    seekBar.progress = player.currentPosition
                    handler.postDelayed(this, 500)
                }
            }
        }

        handler.post(updateTask)
    }

    private fun updateAudioButtons() {
        val isPlaying = mediaPlayer?.isPlaying == true
        btnPlayAudio.visibility = if (isPlaying) View.GONE else View.VISIBLE
        btnPauseAudio.visibility = if (isPlaying) View.VISIBLE else View.GONE
    }

    private fun updateVideoButtons() {
        val isPlaying = videoView.isPlaying
        btnPlayVideo.visibility = if (isPlaying) View.GONE else View.VISIBLE
        btnPauseVideo.visibility = if (isPlaying) View.VISIBLE else View.GONE
    }
}