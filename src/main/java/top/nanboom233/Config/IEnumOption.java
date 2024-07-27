package top.nanboom233.Config;

/**
 * @author Nanboom233
 */
public interface IEnumOption {
    String getStringValue();

    String getTranslationKey();

    IEnumOption getFromValue(String value);

    IEnumOption getFromTranslationKey(String translationKey);
}