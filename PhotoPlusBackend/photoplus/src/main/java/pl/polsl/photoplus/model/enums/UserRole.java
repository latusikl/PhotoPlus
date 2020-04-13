package pl.polsl.photoplus.model.enums;

import lombok.Getter;
import pl.polsl.photoplus.model.exceptions.EnumValueException;

import java.util.Arrays;

@Getter
public enum UserRole
{
    CLIENT("client"), ADMIN("admin");

    private String value;

    UserRole(final String value)
    {
        this.value = value;
    }

    public static UserRole getUserRoleFromString(final String name) throws EnumValueException
    {
        if (name != null) {
            Arrays.stream(UserRole.values())
                    .filter(userRole -> userRole.getValue().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new EnumValueException("Unable to find role for given type", UserRole.class.getSimpleName()));
        }
        throw new EnumValueException("Unable to find role for given type", UserRole.class.getSimpleName());
    }

}
