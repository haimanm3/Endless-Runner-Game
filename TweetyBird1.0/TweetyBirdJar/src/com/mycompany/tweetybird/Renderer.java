package com.mycompany.tweetybird;

import com.mycompany.tweetybird.TweetyBird;
import javax.swing.JPanel;
import java.awt.Graphics;

public class Renderer extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
         if (TweetyBird.tweetybird != null) {
            TweetyBird.tweetybird.repaint(g);
    }
    }
}