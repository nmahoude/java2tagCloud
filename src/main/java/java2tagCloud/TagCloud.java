package java2tagCloud;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class TagCloud {
  
  public void generateTagCloud(List<WordOccurence> occurences) {
    
    final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
    
    List<WordFrequency> wordFrequencies = new ArrayList<>();
    occurences.forEach(occurence -> wordFrequencies.add(new WordFrequency(occurence.getWord(), occurence.getCount())));
    
    final Dimension dimension = new Dimension(600, 600);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
    wordCloud.setPadding(1);
    wordCloud.setBackground(new CircleBackground(300));
    wordCloud.setBackgroundColor(new Color(0x808790));
    wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
    wordCloud.setFontScalar( new SqrtFontScalar(10, 40));
    wordCloud.build(wordFrequencies);
    wordCloud.writeToFile("/home/nicolas/Dev/projects/github/java2tagCloud/wordcloud.png");
  }
}
