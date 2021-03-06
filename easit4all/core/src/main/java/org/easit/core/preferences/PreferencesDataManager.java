package org.easit.core.preferences;

import org.easit.dao.model.EasitAccount;
import org.easit.dao.model.EasitApplicationPreferences;

/**
 * Preferences Manager Interface
 */
public interface PreferencesDataManager {

    void insertOrUpdatePreferences(EasitApplicationPreferences preferences, EasitAccount user) throws Exception;

    EasitApplicationPreferences loadPreferences(EasitAccount user) throws Exception;
}
