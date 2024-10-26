package dashboard.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class CSATOverMonthAreaChartPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color AREA_COLOR = new Color(33, 150, 243, 128);
    private static final Color LINE_COLOR = new Color(33, 150, 243);
    private static final Color HOVER_COLOR = new Color(60, 60, 60);
    private static final Color ACTIVE_COLOR = new Color(80, 80, 80);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");
    private static final int PADDING = 40;
    private static final int POINT_SIZE = 6;

    private String title;
    private TreeMap<Integer, List<Double>> yearData;
    private int currentYear;
    private int minYear;
    private int maxYear;

    public CSATOverMonthAreaChartPanel(String title) {
        this.title = title;
        this.yearData = new TreeMap<>(); // Use TreeMap to keep years sorted
        setPreferredSize(new Dimension(600, 400));
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(createYearTogglePanel(), BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(topPanel, BorderLayout.NORTH);
    }

    private void updateYearTogglePanel() {
        removeAll();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(createYearTogglePanel(), BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(topPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    public void addYearData(int year, List<Double> monthlyData) {
        if (monthlyData.size() != 12) {
            throw new IllegalArgumentException("Monthly data must contain 12 values");
        }
        yearData.put(year, monthlyData);
        if (yearData.size() == 1) {
            currentYear = year;
        }
        minYear = yearData.firstKey();
        maxYear = yearData.lastKey();
        updateYearTogglePanel();
        repaint();
    }

    private JPanel createYearTogglePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel.setOpaque(false);

        ButtonGroup group = new ButtonGroup();

        for (int year : yearData.keySet()) {
            JToggleButton button = createYearToggleButton(year);
            group.add(button);
            panel.add(button);

            if (year == currentYear) {
                button.setSelected(true);
            }
        }

        return panel;
    }

    private JToggleButton createYearToggleButton(int year) {
        JToggleButton button = new JToggleButton(String.valueOf(year)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(ACTIVE_COLOR);
                } else if (getModel().isRollover()) {
                    g2d.setColor(HOVER_COLOR);
                } else if (isSelected()) {
                    g2d.setColor(ACTIVE_COLOR);
                } else {
                    g2d.setColor(BACKGROUND_COLOR);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2d.drawString(getText(), (getWidth() - textWidth) / 2,
                               (getHeight() + textHeight) / 2 - fm.getDescent());
                g2d.dispose();
            }
        };
        
        button.setForeground(TEXT_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(LABEL_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            currentYear = year;
            repaint();
        });
        
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw border
        g2d.setColor(BORDER_COLOR);
        g2d.drawRect(0, 0, width - 1, height - 1);

        // Draw title
        g2d.setFont(TITLE_FONT);
        g2d.setColor(TEXT_COLOR);
        FontMetrics titleFm = g2d.getFontMetrics();
        int titleWidth = titleFm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, PADDING);

        // Draw chart
        if (yearData.containsKey(currentYear)) {
            drawChart(g2d, width, height);
        } else {
            drawNoDataMessage(g2d, width, height);
        }

        g2d.dispose();
    }

    private void drawNoDataMessage(Graphics2D g2d, int width, int height) {
        g2d.setFont(VALUE_FONT);
        g2d.setColor(TEXT_COLOR);
        
         String message="No data available for "+currentYear; 
         FontMetrics fm=g2d.getFontMetrics(); 
         int messageWidth=fm.stringWidth(message); 
         g2d.drawString(message,(width-messageWidth)/2,height/2); 
     }

     private void drawChart(Graphics2D g2d,int width,int height){ 
         int chartWidth=width-2*PADDING; 
         int chartHeight=height-3*PADDING; 
         int baselineY=height-PADDING; 

         List<Double> data=yearData.get(currentYear); 
         double maxValue=Collections.max(data); 
         double minValue=Collections.min(data); 
         double valueRange=maxValue-minValue; 

         // Draw axes 
         g2d.setColor(TEXT_COLOR); 
         g2d.drawLine(PADDING ,baselineY,width-PADDING ,baselineY ); 
         g2d.drawLine(PADDING ,PADDING ,PADDING ,baselineY ); 

         // Draw Y-axis labels 
         g2d.setFont(LABEL_FONT); 
         for(int i=0;i<=5;i++){ 
             double value=minValue+(valueRange*i/5); 
             int y=baselineY-(int)(chartHeight*i/5.0); 
             String label=DECIMAL_FORMAT.format(value); 
             FontMetrics fm=g2d.getFontMetrics(); 
             g2d.drawString(label,PADDING-fm.stringWidth(label)-5,y+fm.getAscent()/2 ); 
         } 

         // Draw X-axis labels 
         String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"}; 
         int xStep=chartWidth/11; 
         for(int i=0;i<12;i++){ 
             int x=PADDING+i*xStep; 
             g2d.drawString(months[i],x ,baselineY+20 ); 
         } 

         // Draw area and line 
         Path2D.Double areaPath=new Path2D.Double(); 
         Path2D.Double linePath=new Path2D.Double(); 

         areaPath.moveTo(PADDING ,baselineY ); 
         for(int i=0;i<12;i++){ 
             int x=PADDING+i*xStep; 
             int y=baselineY-(int)((data.get(i)-minValue)/valueRange*chartHeight); 
             if(i==0){ 
                 areaPath.lineTo(x,y ); 
                 linePath.moveTo(x,y ); 
             }else{ 
                 areaPath.lineTo(x,y ); 
                 linePath.lineTo(x,y ); 
             } 
         } 

         areaPath.lineTo(width-PADDING ,baselineY ); 
         areaPath.closePath(); 

         g2d.setColor(AREA_COLOR); 
         g2d.fill(areaPath); 
         g2d.setColor(LINE_COLOR); 
         g2d.setStroke(new BasicStroke(2f)); 
         g2d.draw(linePath); 

          // Draw data points
          for(int i=0;i<12;i++){  
              int x=PADDING+i*xStep;  
              int y=baselineY-(int)((data.get(i)-minValue)/valueRange*chartHeight);  
              g2d.setColor(LINE_COLOR);  
              g2d.fillOval(x-POINT_SIZE/2,y-POINT_SIZE/2 ,POINT_SIZE ,POINT_SIZE );  
              g2d.setColor(TEXT_COLOR);  
              String value=DECIMAL_FORMAT.format(data.get(i));  
              FontMetrics fm=g2d.getFontMetrics(VALUE_FONT);  
              g2d.setFont(VALUE_FONT);  
              g2d.drawString(value,x-fm.stringWidth(value)/2 ,y-10 );  
          }  
      }  

      public static void main(String[] args){  
          SwingUtilities.invokeLater(()->{  
              JFrame frame=new JFrame("CSAT Over Month Area Chart");  
              CSATOverMonthAreaChartPanel chart=new CSATOverMonthAreaChartPanel("CSAT Trends Over Months");  

              // Sample data  
              chart.addYearData(2021 ,Arrays.asList(75.0 ,78.0 ,80.0 ,79.0 ,82.0 ,85.0 ,87.0 ,86.0 ,88.0 ,90.0 ,89.0 ,91.0));  
              chart.addYearData(2022 ,Arrays.asList(80.0 ,82.0 ,85.0 ,84.0 ,86.0 ,88.0 ,90.0 ,89.0 ,91.0 ,93.0 ,92.0 ,94.0));  
              chart.addYearData(2023 ,Arrays.asList(85.0 ,87.0 ,89.0 ,88.0 ,90.0 ,92.0 ,94.0 ,93.0 ,95.0 ,96.0 ,95.0 ,97.0));  

              frame.add(chart);  
              frame.pack();  
              frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
              frame.setLocationRelativeTo(null);  
              frame.setVisible(true);  
          });  
      }  

}