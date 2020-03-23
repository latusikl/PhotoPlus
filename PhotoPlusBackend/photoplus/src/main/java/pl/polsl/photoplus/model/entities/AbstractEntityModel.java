package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.security.SecureRandom;

/**
 * Base class for all entities.
 * Contains all common values for model objects.
 * After extending that class the columns from that class are inserted to entity.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntityModel
{
    @Transient
    protected final int CODE_NAME_LENGTH = 4;

    @Transient
    protected final int CODE_NUMBER_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected long id;

    @Column(name = "model_code")
    protected @NotBlank String code;

    /**
     * Function is responsible for generating code for given model type.
     * Code should consist of generated numbers
     *
     * @see #createCodeNumber()
     * and from alphabetical part based on e.g name of given length.
     * @see #CODE_NAME_LENGTH
     * Minimal length of taken attribute should be validated!
     * Should be invoked after changing attribute value on which code is based!
     */
    @PostConstruct
    protected abstract void generateModelCode();

    protected String createCodeNumber()
    {
        final SecureRandom secureRandom = new SecureRandom();
        final long secureNumber = id + Math.abs(secureRandom.nextLong());
        final String codeNumber = Long.toString(secureNumber);
        return codeNumber.substring(0, CODE_NUMBER_LENGTH);
    }

}
