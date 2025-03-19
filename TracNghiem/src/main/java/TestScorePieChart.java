import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestScorePieChart extends JFrame {
    
    private Map<String, int[]> testResults; // Dữ liệu số lượng thí sinh tham gia từng bài test

    public TestScorePieChart() {
        setTitle("Thống kê số lượng thí sinh tham gia bài test");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Dữ liệu giả lập: "Bài Test" -> {số lượng < 5, số lượng >= 5, tổng số thí sinh}
        testResults = new HashMap<>();
        testResults.put("Bài Test 1", new int[]{30, 70, 100});
        testResults.put("Bài Test 2", new int[]{50, 50, 100});
        testResults.put("Bài Test 3", new int[]{40, 60, 100});

        // Tạo biểu đồ cột
        JPanel chartPanel = createChartPanel(testResults);
        setContentPane(chartPanel);
    }

    // Hàm tạo biểu đồ cột
    private JPanel createChartPanel(Map<String, int[]> testResults) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String testName : testResults.keySet()) {
            int[] scores = testResults.get(testName);
            int below5 = scores[0];  // Số lượng thí sinh < 5 điểm
            int aboveOrEqual5 = scores[1]; // Số lượng thí sinh >= 5 điểm
            int total = scores[2];  // Tổng số thí sinh

            dataset.addValue(total, "Tổng số thí sinh", testName);
            dataset.addValue(aboveOrEqual5, "Thí sinh >= 5 điểm", testName);
            dataset.addValue(below5, "Thí sinh < 5 điểm", testName);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lượng thí sinh tham gia bài test", // Tiêu đề biểu đồ
                "Bài Test", // Nhãn trục X
                "Số lượng thí sinh", // Nhãn trục Y
                dataset
        );

        // Tuỳ chỉnh màu sắc
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(102, 178, 255)); // Màu xanh (Tổng số thí sinh)
        renderer.setSeriesPaint(1, new Color(102, 204, 102)); // Màu xanh lá (Thí sinh >= 5 điểm)
        renderer.setSeriesPaint(2, new Color(255, 102, 102)); // Màu đỏ (Thí sinh < 5 điểm)

        return new ChartPanel(barChart);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TestScorePieChart().setVisible(true);
        });
    }
}
