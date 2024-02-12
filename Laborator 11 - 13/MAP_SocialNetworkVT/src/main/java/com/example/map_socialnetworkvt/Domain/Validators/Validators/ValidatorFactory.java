package com.example.map_socialnetworkvt.Domain.Validators.Validators;
import com.example.map_socialnetworkvt.Exception.ValidationException;
public class ValidatorFactory implements Factory {
    private static ValidatorFactory instance;
    @Override
    public Validator createValidator(ValidatorStrategy strategy) {
        switch (strategy){
            case User -> {return new UserValidator();}
            case Friendship -> {return new FriendshipValidator();}
            default -> throw new ValidationException("Invalid Strategy!");
        }

    }

    /**
     *
     * @return instance if it doesn't exist
     */
    public static ValidatorFactory getInstance()
    {
        if(instance==null)
        {
            instance=new ValidatorFactory();
        }
        return instance;
    }
}
