package Resources;

import Exceptions.InsufficientFundsException;

public interface ExecuteTransaction {
    void execute() throws InsufficientFundsException;

}
