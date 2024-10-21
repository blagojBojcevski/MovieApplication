package com.test.movieapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.test.movieapplication.BuildConfig
import com.test.movieapplication.data.model.Movie
import coil.request.CachePolicy
import coil.request.ImageRequest

import com.test.movieapplication.R


@Composable
fun MovieItem(movie: Movie, onMovieClick: (String) -> Unit, isOnline: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onMovieClick(movie.id.toString()) }
    ) {
        MovieImage(
            moviePosterPath = movie.poster_path ?: "",
            isOnline = isOnline,
            modifier = Modifier.height(200.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = movie.original_title ?: "",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = movie.overview ?: "",
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MovieImage(
    moviePosterPath: String,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("${BuildConfig.IMAGE_URL}$moviePosterPath")
            .apply {
                if (!isOnline) {
                    networkCachePolicy(CachePolicy.DISABLED)
                }
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_error_image)
            }
            .build()
    )

    val painterState = painter.state

    val contentScale = when (painterState) {
        is AsyncImagePainter.State.Loading -> ContentScale.Fit
        is AsyncImagePainter.State.Error -> ContentScale.Fit
        else -> ContentScale.Crop
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}
