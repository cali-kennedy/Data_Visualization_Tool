import javax.swing.*;
import java.awt.*;

public class DetailsPanel extends JPanel {

    private JLabel artistAndTitleLabel;
    private JLabel artistLabel;
    private JLabel totalStreamsLabel;
    private JLabel dailyStreamsLabel;
    private JLabel yearLabel;
    private JLabel mainGenreLabel;
    private JTextArea genresTextArea; // Use JTextArea for genres
    private JLabel firstGenreLabel;
    private JLabel secondGenreLabel;
    private JLabel thirdGenreLabel;

    public DetailsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking

        // Font settings for the labels
        Font boldFont = new Font("Arial", Font.BOLD, 14);
        Font regularFont = new Font("Arial", Font.PLAIN, 14);

        // Initialize labels
        artistAndTitleLabel = createStyledLabel("", regularFont);
        artistLabel = createStyledLabel("", regularFont);
        totalStreamsLabel = createStyledLabel("", regularFont);
        dailyStreamsLabel = createStyledLabel("", regularFont);
        yearLabel = createStyledLabel("", regularFont);
        mainGenreLabel = createStyledLabel("", regularFont);

        // Use JTextArea for genres with a JScrollPane
        genresTextArea = new JTextArea(3, 20); // Allow 3 rows of text, 20 characters wide
        genresTextArea.setEditable(false);
        genresTextArea.setLineWrap(true);
        genresTextArea.setWrapStyleWord(true);
        JScrollPane genresScrollPane = new JScrollPane(genresTextArea);

        firstGenreLabel = createStyledLabel("", regularFont);
        secondGenreLabel = createStyledLabel("", regularFont);
        thirdGenreLabel = createStyledLabel("", regularFont);

        // Add labels to panel (BoxLayout ensures vertical stacking)
        add(createGroupPanel("Artist and Title:", artistAndTitleLabel, boldFont));
        add(createGroupPanel("Artist:", artistLabel, boldFont));
        add(createGroupPanel("Total Streams:", totalStreamsLabel, boldFont));
        add(createGroupPanel("Daily Streams:", dailyStreamsLabel, boldFont));
        add(createGroupPanel("Year:", yearLabel, boldFont));
        add(createGroupPanel("Main Genre:", mainGenreLabel, boldFont));

        // Add genres with a scroll pane
        add(createGroupPanel("Genres:", genresScrollPane, boldFont)); // Add scroll pane for genres

        add(createGroupPanel("First Genre:", firstGenreLabel, boldFont));
        add(createGroupPanel("Second Genre:", secondGenreLabel, boldFont));
        add(createGroupPanel("Third Genre:", thirdGenreLabel, boldFont));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel
    }

    // Helper method to create a panel for a title and corresponding data label or component
    private JPanel createGroupPanel(String title, JComponent dataComponent, Font titleFont) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(50, 50, 50)); // Dark color for titles
        panel.add(titleLabel, BorderLayout.NORTH); // Align titles to the top
        panel.add(dataComponent, BorderLayout.CENTER); // Add the data component below
        return panel;
    }

    // Updates the details in the panel when a table row is selected
    public void updateDetails(DataModel selectedItem) {
        artistAndTitleLabel.setText(selectedItem.getArtist_and_title());
        artistLabel.setText(selectedItem.getArtist());
        totalStreamsLabel.setText(String.format("%,.0f", selectedItem.getTotal_streams()));
        dailyStreamsLabel.setText(String.format("%.1f", selectedItem.getDaily_streams()));
        yearLabel.setText(String.format("%,.0f", selectedItem.getYear()));
        mainGenreLabel.setText(selectedItem.getMain_genre());

        // Update genres area with scroll support
        genresTextArea.setText(String.join(", ", selectedItem.getGenres()));

        firstGenreLabel.setText(selectedItem.getFirst_genre());
        secondGenreLabel.setText(selectedItem.getSecond_genre());
        thirdGenreLabel.setText(selectedItem.getThird_genre());
    }

    // Helper method to create styled labels
    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }
}
