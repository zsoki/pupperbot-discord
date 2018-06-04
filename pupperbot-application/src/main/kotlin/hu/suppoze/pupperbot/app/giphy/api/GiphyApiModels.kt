package hu.suppoze.pupperbot.app.giphy.api

data class GiphyRandomResponse(
    val data: GiphyGifObject,
    val meta: GiphyMetaObject
)

data class GiphySearchResponse(
    val data: List<GiphyGifObject>,
    val pagination: GiphyPaginationObject,
    val meta: GiphyMetaObject
)

data class GiphyGifObject(
    val type: String,
    val id: String,
    val url: String,
    val image_original_url: String,
    val image_url: String,
    val image_mp4_url: String,
    val image_frames: String,
    val image_width: String,
    val image_height: String,
    val fixed_height_downsampled_url: String,
    val fixed_height_downsampled_width: String,
    val fixed_height_downsampled_height: String,
    val fixed_width_downsampled_url: String,
    val fixed_width_downsampled_width: String,
    val fixed_width_downsampled_height: String,
    val fixed_height_small_url: String,
    val fixed_height_small_still_url: String,
    val fixed_height_small_width: String,
    val fixed_height_small_height: String,
    val fixed_width_small_url: String,
    val fixed_width_small_still_url: String,
    val fixed_width_small_width: String,
    val fixed_width_small_height: String
)

data class GiphyPaginationObject(
    val offset: Int,
    val total_count: Int,
    val count: Int
)

data class GiphyMetaObject(
    val status: String,
    val msg: String
)