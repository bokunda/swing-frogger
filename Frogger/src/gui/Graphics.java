package gui;

import engine.Directions;
import engine.Engine;
import engine.Fields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Graphics extends JFrame implements KeyEventDispatcher
{
    private int height;
    private int width;

    private final int fieldWidth = 36;
    private final int fieldHeight = 36;

    Engine game;

    Timer t;

    JLabel title;
    JLabel level;
    JLabel lives;

    private JLabel[][] fields;

    public Graphics(int height, int width)
    {
        super("Frogger");
        this.height = height;
        this.width = width;

        this.game = new Engine(height, width);
        this.fields = new JLabel[height][width];

        setGame();
        setStats();
        setNewGame();
        setMusic();

        refreshGUI();
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setStats()
    {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 3));
        p.setBackground(Color.WHITE);

        this.lives = new JLabel();
        this.lives.setPreferredSize(new Dimension(16, 16));
        this.lives.setIcon(this.getLives(game.getLives()));

        this.title = new JLabel("Frogger");
        this.title.setHorizontalAlignment(JLabel.CENTER);

        this.level = new JLabel("Level: 1");
        this.level.setHorizontalAlignment(JLabel.RIGHT);

        p.add(this.lives);
        p.add(this.title);
        p.add(this.level);


        getContentPane().add(p, BorderLayout.NORTH);
    }

    public void setMusic()
    {
        String soundName = "src/gui/sound/soundtrack.wav";
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(1991);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void jumpSound()
    {
        String soundName = "src/gui/sound/jump.wav";
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setNewGame()
    {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);

        JButton btn = new JButton("New game");
        btn.setPreferredSize(new Dimension((this.width - 1) * this.fieldWidth, 30));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> game = new Engine(this.height, this.width));

        p.add(btn);

        getContentPane().add(p, BorderLayout.SOUTH);
    }

    public void setGame()
    {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(this.height, this.width));

        for(int i = 0; i < this.height; i++)
        {
            for(int j = 0; j < this.width; j++)
            {
                this.fields[i][j] = new JLabel();
                this.fields[i][j].setPreferredSize(new Dimension(fieldHeight, fieldWidth));
                this.fields[i][j].setIcon(this.getImage(i, j));
                p.add(this.fields[i][j]);
            }
        }

        getContentPane().add(p, BorderLayout.CENTER);

        t = new Timer(200, e ->
        {
            refreshGUI();
        });

        t.start();

        pack();

    }

    public void refreshGUI()
    {
        for(int i = 0; i < this.height; i++)
        {
            for(int j = 0; j < this.width; j++)
            {
                this.fields[i][j].removeAll();
                this.fields[i][j].revalidate();
                this.fields[i][j].repaint();

                this.fields[i][j].setIcon(this.getImage(i, j));
            }
        }

        this.lives.setIcon(this.getLives(game.getLives()));
        this.level.setText("Level: " + game.getLevel());

        game.moveObjects();

        game.spawnObject();
        if(game.getLevel() > 1) game.spawnObject();
        if(game.getLevel() > 2) game.spawnObject();

        if(game.isCollided() == true || game.isOnGoal())
        {
            if(game.isEnd() == false)
            {
                game.initFrog();
            }
            else
            {
                String msg = "Game over!";

                if(game.getLives() > 0) msg = "You win!";

                if(JOptionPane.showConfirmDialog(null, "" + msg + "\nSteps: " + game.getSteps() + "\nPlay again?", msg, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
                {
                    game = new Engine(height, width);
                    refreshGUI();
                }
                else
                {
                    System.exit(0);
                }
            }
        }
        pack();
    }

    public ImageIcon getLives(int lives)
    {
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/lives" + lives + ".png")));
    }

    public ImageIcon getImage(int i, int j)
    {
        if(game.getFieldValue(i, j) == Fields.Grass)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/grass.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Frog)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/frog.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Road)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/road.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Water)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/water.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Goal)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/goal.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Wood)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/wood.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Car)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/car.png")));
        }
        else if(game.getFieldValue(i, j) == Fields.Bus)
        {
            return new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/bus.png")));
        }

        return null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e)
    {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
            this.jumpSound();
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    game.move(Directions.Up);
                    break;
                case KeyEvent.VK_DOWN:
                    game.move(Directions.Down);
                    break;
                case KeyEvent.VK_LEFT:
                    game.move(Directions.Left);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.move(Directions.Right);
                    break;
            }
        }
        return false;
    }
}
