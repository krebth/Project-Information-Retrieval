
public class Song {
	private String num;
	private String artist;
	private String title;
	private String album;
	private String year;
	private String date;
	private String lyric;
	
	public Song(String num, String artist, String title, String album, String year, String date, String lyric) {
		this.num = num;
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.year = year;
		this.date = date;
		this.lyric = lyric;
	}
	
	public String getNum() {
		return num;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getLyric() {
		return lyric;
	}
	
	public void setNum(String num) {
		this.num = num;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}
