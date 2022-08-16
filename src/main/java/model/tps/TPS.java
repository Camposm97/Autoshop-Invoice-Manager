package model.tps;

import java.util.LinkedList;
import java.util.List;

public class TPS {
    public interface Transaction {
        void doTransaction();
        void undoTransaction();
    }
    private List<Transaction> transactions;
    private int numTransactions;
    private int mostRecentTransaction;
    private boolean performingDo, performingUndo;

    public TPS() {
        this.transactions = new LinkedList<>();
        this.numTransactions = 0;
        this.mostRecentTransaction = -1;
        this.performingDo = false;
        this.performingUndo = false;
    }

    public int getSize() {
        return transactions.size();
    }

    public int getRedoSize() {
        return getSize() - mostRecentTransaction - 1;
    }

    public int getUndoSize() {
        return mostRecentTransaction + 1;
    }

    public boolean isPerformingDo() {
        return performingDo;
    }

    public boolean isPerformingUndo() {
        return performingUndo;
    }

    public boolean hasTransactionToRedo() {
        return mostRecentTransaction + 1 < numTransactions;
    }

    public boolean hasTransactionToUndo() {
        return mostRecentTransaction >= 0;
    }

    public void addTransaction(Transaction t) {
        if ((this.mostRecentTransaction < 0)
                || (this.mostRecentTransaction < (this.transactions.size() - 1))) {
            for (int i = this.transactions.size() - 1; i > this.mostRecentTransaction; i--) {
                this.transactions = this.transactions.subList(i, 1);
            }
            this.numTransactions = this.mostRecentTransaction + 2;
        }
        else {
            this.numTransactions++;
        }

        // ADD THE TRANSACTION
        this.transactions.add(t);
//        this.transactions[this.mostRecentTransaction+1] = transaction;

        // AND EXECUTE IT
        doTransaction();
    }

    /**
     * doTransaction
     *
     * Does the current transaction on the stack and advances the transaction
     * counter. Note this function may be invoked as a result of either adding
     * a transaction (which also does it), or redoing a transaction.
     */
    public void doTransaction() {
        if (this.hasTransactionToRedo()) {
            this.performingDo = true;
            Transaction transaction = transactions.get(mostRecentTransaction + 1);
            transaction.doTransaction();
            this.mostRecentTransaction++;
            this.performingDo = false;
        }
    }

    /**
     * This function gets the most recently executed transaction on the
     * TPS stack and undoes it, moving the TPS counter accordingly.
     */
    public void undoTransaction() {
        if (this.hasTransactionToUndo()) {
            this.performingUndo = true;
            Transaction transaction = transactions.get(mostRecentTransaction);
            transaction.undoTransaction();
            this.mostRecentTransaction--;
            this.performingUndo = false;
        }
    }

    /**
     * clearAllTransactions
     *
     * Removes all the transactions from the TPS, leaving it with none.
     */
    public void clearAllTransactions() {
        // REMOVE ALL THE TRANSACTIONS
        this.transactions.clear();

        // MAKE SURE TO RESET THE LOCATION OF THE
        // TOP OF THE TPS STACK TOO
        this.mostRecentTransaction = -1;
        this.numTransactions = 0;
    }

    /**
     * toString
     *
     * Builds and returns a textual representation of the full TPS and its stack.
     */
    @Override
    public String toString() {
        String text = "--Number of Transactions: " + this.numTransactions + "\n";
        text += "--Current Index on Stack: " + this.mostRecentTransaction + "\n";
        text += "--Current Transaction Stack:\n";
        for (int i = 0; i <= this.mostRecentTransaction; i++) {
            Transaction jT = this.transactions.get(i);
            text += "----" + jT.toString() + "\n";
        }
        return text;
    }
}
