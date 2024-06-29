package top.nanboom233.Config;

/**
 * @author Nanboom233
 */
public interface IConfigOption {
    String getStringValue();

    String getTranslationKey();

    IConfigOption getFromValue(String value);

    IConfigOption getFromTranslationKey(String translationKey);
}