package in.xiandan.mmrc;

import android.media.MediaMetadataRetriever;

/**
 * @author dengyuhan
 * created 2019-07-03 17:53
 */
public interface MediaMetadataKey {

    //最接近timeUs的关键帧 - 仅视频
    int OPTION_CLOSEST_SYNC = MediaMetadataRetriever.OPTION_CLOSEST_SYNC;
    //最接近timeUs的帧，不一定是关键帧(性能开销较大) - 仅视频
    int OPTION_CLOSEST = MediaMetadataRetriever.OPTION_CLOSEST;
    //早于timeUs的关键帧 - 仅视频
    int OPTION_PREVIOUS_SYNC = MediaMetadataRetriever.OPTION_PREVIOUS_SYNC;
    //晚于timeUs的关键帧 - 仅视频
    int OPTION_NEXT_SYNC = MediaMetadataRetriever.OPTION_NEXT_SYNC;

    String WIDTH = "width";
    String HEIGHT = "height";
    String DURATION = "duration";
    String ROTATION = "rotation";
    String FRAMERATE = "frame_rate";
    String ALBUM = "album";
    String ARTIST = "artist";
    String ALBUMARTIST = "album_artist";
    String COMPOSER = "composer";
    String DATE = "date";
    String GENRE = "genre";
    String TITLE = "title";
    String NUM_TRACKS = "num_tracks";
    String DISC_NUMBER = "disc_number";
    //android
    String CD_TRACK_NUMBER = "cd_track_number";
    String AUTHOR = "author";
    String YEAR = "year";
    String WRITER = "writer";
    String MIMETYPE = "mime_type";
    String COMPILATION = "compilation";
    String HAS_AUDIO = "has_audio";
    String HAS_VIDEO = "has_video";
    String BITRATE = "bit_rate";
    String LOCATION = "location";
    String HAS_IMAGE = "has_imag";
    String IMAGE_COUNT = "image_count";
    String IMAGE_PRIMARY = "image_primary";
    String FRAME_COUNT = "frame_count";
    //ffmpeg
    String COMMENT = "comment";
    String COPYRIGHT = "copyright";
    String ENCODER = "encoder";
    String ENCODED_BY = "encoded_by";
    String FILENAME = "file_name";
    String LANGUAGE = "language";
    String PERFORMER = "performer";
    String PUBLISHER = "publisher";
    String SERVICE_NAME = "service_name";
    String SERVICE_PROVIDER = "service_provider";
    String AUDIO_CODEC = "audio_codec";
    String VIDEO_CODEC = "video_codec";
    String ICY_METADATA = "icy_metadata";
    String CHAPTER_START_TIME = "chapter_start_time";
    String CHAPTER_END_TIME = "chapter_end_time";
    String CHAPTER_COUNT = "chapter_count";
    String FILESIZE = "file_size";
}
