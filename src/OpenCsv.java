import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.tartarus.snowball.ext.PorterStemmer;
//import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.search.highlight.Highlighter;


public class OpenCsv {
	private Path indexPath;
	private Directory indexDir;
	private AnalyzeText analyzeTXT;
	private ArrayList<Song> allSongs;
	private String[] inputFields;
	private String[] inputTerms;
	private String[] inputTermsWithoutStem;
	
	public void openCsv(File csvFile) throws IOException, java.text.ParseException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(csvFile);
		} catch (FileNotFoundException e) {
			System.out.println("CSV file not found!");
			e.printStackTrace();
		}
	
		indexPath = Paths.get("./files/index");
        indexDir = FSDirectory.open(indexPath);
        
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDir, writerConfig);
		
        analyzeTXT = new AnalyzeText();
        int line = 0;
        String[] header = scanner.nextLine().split(",");
        for (int i = 0; i < header.length; i++) {
        	header[i] = header[i].toLowerCase();
        }
        line ++;
        allSongs = new ArrayList<Song>();
        while (scanner.hasNextLine()) {
            String[] row = scanner.nextLine().split(",");
            Document doc = new Document();
            System.out.println(line + " " + row.length);
            Song newSong = new Song(row[0],row[1],row[2],row[3],row[4],row[5],row[6]);
            for (int i = 0; i < row.length; i++) {
                String fieldName = header[i];
                String fieldValue = row[i];
                if (i != 4 && i != 5) {
                	fieldValue = analyzeTXT.toLowerCase(fieldValue);
                	if (i == 2) {
                    	fieldValue = analyzeTXT.stemText(fieldValue);
                	}
                    if (i == 6) {
                    	fieldValue = analyzeTXT.removeStopWords(fieldValue);
                    	fieldValue = analyzeTXT.stemText(fieldValue);
                    }
                }
                System.out.println(fieldValue);
                doc.add(new TextField(fieldName, fieldValue, Field.Store.YES));
            }
            allSongs.add(newSong);
            line ++;
            indexWriter.addDocument(doc);
        }
        
        
        scanner.close();
        indexWriter.close();
        
        System.out.println("Indexing complete!");
	}
	
	public ArrayList<ArrayList<String>> read(String lines, String terms) throws IOException, java.text.ParseException {
    	DirectoryReader ireader = DirectoryReader.open(indexDir);
    	IndexSearcher isearcher = new IndexSearcher(ireader);
    	
    	
    	inputFields = lines.split(",");
    	for (int i = 0; i < inputFields.length; i++) {
    		inputFields[i] = inputFields[i].toLowerCase();
    	}
    	
    	inputTerms = terms.split(",");
    	inputTermsWithoutStem = terms.split(",");
    	
    	PorterStemmer stemmer = new PorterStemmer();
    	for (int i = 0; i < inputTerms.length; i++) {
    		if (!isDate(inputTerms[i])) {
    			inputTerms[i] = inputTerms[i].toLowerCase();
    			inputTermsWithoutStem[i] = inputTermsWithoutStem[i].toLowerCase();
        		stemmer.setCurrent(inputTerms[i]);
            	stemmer.stem();
            	inputTerms[i] = stemmer.getCurrent();
    		}
    	}
    			
    	BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        for (String field : inputFields) {
            for (String term : inputTerms) {
                Query termQuery = new TermQuery(new Term(field, term));
                booleanQueryBuilder.add(new BooleanClause(termQuery, BooleanClause.Occur.SHOULD));
            }
        }
        
        BooleanQuery booleanQuery = booleanQueryBuilder.build();

        TopDocs topDocs = isearcher.search(booleanQuery, 1000);
        ScoreDoc[] hits = topDocs.scoreDocs;
        
        ArrayList<ArrayList<String>> allResults = new ArrayList<ArrayList<String>>();
        for (ScoreDoc hit : hits) {
            Document doc = isearcher.doc(hit.doc);
            String numStr = doc.get("num");
            numStr = numStr.trim(); 
            int num = Integer.parseInt(numStr); 
            System.out.println(num);
            Song song = allSongs.get(num);
            allResults.add(boldMatchingWords(song, inputTermsWithoutStem, inputFields));
        }
    	
        System.out.println("Done!");
    	ireader.close();
    	indexDir.close();
    	deleteFolder();
    	return allResults;
	}
	
	public void deleteFolder() {
		try {
            Files.walk(indexPath)
                 .sorted((p1, p2) -> -p1.compareTo(p2))
                 .forEach(p -> {
                     try {
                         Files.delete(p);
                     } catch (Exception e) {
                         System.err.println("Failed to delete " + p);
                     }
                 });
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public String convertDate(String strDate) throws java.text.ParseException{
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy MM dd");
	    Date date = null;
	    date = inputFormat.parse(strDate);
	    String formattedDate = outputFormat.format(date);
	    return formattedDate;
	}
	
	public boolean isDate(String strDate) throws java.text.ParseException{
		String[] formats = {"yyyy-MM-dd", "yyyy-MM", "yyyy"};
		for (String format : formats) {
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        sdf.setLenient(false);
	        try {
	            sdf.parse(strDate);
	            return true;
	        } catch (Exception e) {
	        }
	    }
	    return false;
	}

	
	public ArrayList<String> boldMatchingWords(Song song, String[] values, String[] fields) {
		ArrayList<String> result = new ArrayList<String>();
	    for (String field: fields) {
	    	for (String value: values) {
	    		if (field.equals("num")) {
	    			if (song.getNum().equals(value)) {
	    				song.setNum("<b>" + value + "</b>");
	    			} else {
	    				continue;
	    			}
	    		} else if (field.equals("year")) {
	    			if (song.getYear().equals(value)) {
	    				song.setYear("<b>" + value + "</b>");
	    			} else {
	    				continue;
	    			}
	    		} else if (field.equals("artist")) {
	    			List<String> aList = Arrays.asList(song.getArtist().split(" "));
	    			for (int i = 0; i < aList.size(); i++) {
	    	            String lowerCaseString = aList.get(i).toLowerCase();
	    	            aList.set(i, lowerCaseString);
	    	        }
	    			if (aList.contains(value)) {
	    				aList.set(aList.indexOf(value), "<b>" + value + "</b>");
	    			}
	    			song.setArtist(String.join(" ", aList));
	    			
	    		} else if (field.equals("title")) {
	    			List<String> aList = Arrays.asList(song.getTitle().split(" "));
	    			for (int i = 0; i < aList.size(); i++) {
	    	            String lowerCaseString = aList.get(i).toLowerCase();
	    	            aList.set(i, lowerCaseString);
	    	        }
	    			if (aList.contains(value)) {
	    				aList.set(aList.indexOf(value), "<b>" + value + "</b>");
	    			}
	    			song.setTitle(String.join(" ", aList));
	    		} else if (field.equals("album")) {
	    			List<String> aList = Arrays.asList(song.getAlbum().split(" "));
	    			for (int i = 0; i < aList.size(); i++) {
	    	            String lowerCaseString = aList.get(i).toLowerCase();
	    	            aList.set(i, lowerCaseString);
	    	        }
	    			if (aList.contains(value)) {
	    				aList.set(aList.indexOf(value), "<b>" + value + "</b>");
	    			}
	    			song.setAlbum(String.join(" ", aList));
	    		}  else if (field.equals("lyric")) {
	    			List<String> aList = Arrays.asList(song.getLyric().split(" "));
	    			for (int i = 0; i < aList.size(); i++) {
	    	            String lowerCaseString = aList.get(i).toLowerCase();
	    	            aList.set(i, lowerCaseString);
	    	        }
	    			if (aList.contains(value)) {
	    				aList.set(aList.indexOf(value), "<b>" + value + "</b>");
	    			}
	    			song.setLyric(String.join(" ", aList));
	    		} else if (field.equals("date")) {
	    			List<String> aList = Arrays.asList(song.getDate().split("-"));
	    			for (int i = 0; i < aList.size(); i++) {
	    	            String lowerCaseString = aList.get(i).toLowerCase();
	    	            aList.set(i, lowerCaseString);
	    	        }
	    			if (aList.contains(value)) {
	    				aList.set(aList.indexOf(value), "<b>" + value + "</b>");
	    			}
	    			song.setDate(String.join("-", aList));
	    		}
	    	}
	    }
	    
	    result.add("<html>" + song.getNum() + "</html>");
	    result.add("<html>" + song.getArtist() + "</html>");
	    result.add("<html>" + song.getTitle() + "</html>");
	    result.add("<html>" + song.getAlbum() + "</html>");
	    result.add("<html>" + song.getYear() + "</html>");
	    result.add("<html>" + song.getDate() + "</html>");
	    result.add("<html>" + song.getLyric() + "</html>");
	    return result;
		
	}

	public String[] getFields() {
		return inputFields;
	}
	
	public String[] getTerms() {
		return inputTerms;
	}

	
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
		OpenCsv open = new OpenCsv();
		//open.openCsv();
		open.read("Artist","Ariana Grande");
	}
}
