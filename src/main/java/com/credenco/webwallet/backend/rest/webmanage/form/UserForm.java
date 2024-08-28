package com.credenco.webwallet.backend.rest.webmanage.form;


import com.credenco.webwallet.backend.rest.webmanage.dto.UserPreferenceDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    private String locale;
    private List<UserPreferenceDto> userPreferences;

}
