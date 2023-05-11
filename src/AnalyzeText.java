import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AnalyzeText {
	private Analyzer analyzer;
	private TokenStream stream;
    
    public void setStopWords() {
        List<String> stopWords = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with");
        CharArraySet stopSet = new CharArraySet(stopWords, true);
        analyzer = new StopAnalyzer(stopSet);
    }
    
    public String removeStopWords(String text) throws IOException {
    	setStopWords();
        stream = analyzer.tokenStream(null, text);
        stream.reset();
        String txt = "";
        while (stream.incrementToken()) {
            CharTermAttribute term = stream.getAttribute(CharTermAttribute.class);
            txt += term.toString() + " ";
        }
        stream.end();
        stream.close();
        return txt;
    }

    public String stemText(String text) throws IOException {
        Analyzer stemAnalyzer = new Analyzer() {
            protected TokenStreamComponents createComponents(String fieldName) {
                WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
                TokenStream tokenStream = new PorterStemFilter(tokenizer);
                return new TokenStreamComponents(tokenizer, tokenStream);
            }
        };
        stream = stemAnalyzer.tokenStream(null, text);
        CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        String txt = "";
        while (stream.incrementToken()) {
            String term = charTermAttribute.toString();
            txt += term.toString() + " ";
        }
        stream.end();
        stream.close();
        return txt;
    }
    
    public String toLowerCase(String text) throws IOException {
    	Analyzer analyzer = new StandardAnalyzer();
    	stream = analyzer.tokenStream(null, text);
    	stream = new LowerCaseFilter(stream); 
    	CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);
    	stream.reset();
    	String txt = "";
    	while (stream.incrementToken()) {
    	    String term = charTermAttribute.toString();
    	    txt += term + " ";
    	}
    	return txt;
    }
    
    public String convertDate(String strDate) throws ParseException{
    	SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy MM dd");
	    Date date = null;
	    date = inputFormat.parse(strDate);
	    String formattedDate = outputFormat.format(date);
	    System.out.println(formattedDate + "aaaaa");
	    return formattedDate;
    }
}
