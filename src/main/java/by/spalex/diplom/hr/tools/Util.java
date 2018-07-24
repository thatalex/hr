package by.spalex.diplom.hr.tools;

import by.spalex.diplom.hr.model.Pretender;
import by.spalex.diplom.hr.model.Skill;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Utility class for general purpose methods
 */
public enum Util {
    ;
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final int IMAGE_SIZE = 200;
    public static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * @return current time truncated to minutes
     */
    public static LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(MINUTES);
    }

    /**
     * Checks string is null or empty
     *
     * @param value checked string
     * @return true of string is null or empty, false otherwise
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static double round(double value, int scale) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * return bytes as Base64 string
     */
    public static String encodeImageToString(byte[] bytes) {
        byte[] result = bytes;
        try (InputStream inputStream = new ByteArrayInputStream(result);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Image image = new Image(inputStream, IMAGE_SIZE, IMAGE_SIZE, true, true);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            if (bufferedImage.getHeight() > bufferedImage.getWidth()) {
                int delta = bufferedImage.getHeight() - bufferedImage.getWidth();
                bufferedImage = bufferedImage.getSubimage(0, delta / 2, bufferedImage.getWidth(), bufferedImage.getHeight() - delta);
            } else if (bufferedImage.getWidth() > bufferedImage.getHeight()) {
                int delta = bufferedImage.getWidth() - bufferedImage.getHeight();
                bufferedImage = bufferedImage.getSubimage(delta / 2, 0, bufferedImage.getWidth() - delta, bufferedImage.getHeight());
            }
            ImageIO.write(bufferedImage, "png", outputStream);
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Base64.Encoder base64_enc = Base64.getEncoder();
        return base64_enc.encodeToString(result);
    }

    /**
     * set image of pretender from given file
     * @param file      file of image
     * @param pretender pretender for image settling
     */
    public static void setImage(MultipartFile file, Pretender pretender) throws IOException {
        if (file != null) {
            byte[] image = file.getBytes();
            if (image != null && image.length > 0) {
                pretender.setImage(Util.encodeImageToString(image));
            }
        }
    }

    /**
     * Sort pretenders by it's skill rates
     * @param pretenders list to sort
     * @param skill skill to sort by rating or null in case of sorting by summary rating of all skills
     */
    public static void sortBySkillRate(List<Pretender> pretenders, Skill skill) {
        Collections.sort(pretenders, (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            } else if (o2 == null) {
                return -1;
            } else if (o1 == null) {
                return 1;
            } else {
                if (skill != null) {
                    Double value1 = o1.getSkillRate(skill);
                    Double value2 = o2.getSkillRate(skill);
                    if (value1 == null) {
                        return -1;
                    } else if (value2 == null) {
                        return 1;
                    } else {
                        int c = value2.compareTo(value1);
                        if (c != 0) {
                            return c;
                        }
                    }
                }
                Double value1 = o1.getSkillRates();
                Double value2 = o2.getSkillRates();
                return value1 == null ? -1 : value2 == null ? 1 : value2.compareTo(value1);
            }
        });
    }

    /**
     * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt.
     */
    public static String encode(String value) {
        return passwordEncoder.encode(value);
    }
}

