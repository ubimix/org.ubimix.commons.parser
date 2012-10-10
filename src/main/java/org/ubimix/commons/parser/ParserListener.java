package org.ubimix.commons.parser;

public class ParserListener implements IParserListener {

    private boolean fReportErrors;

    @Override
    public void onError(String message, Throwable error) {
        if (error instanceof IllegalStateException) {
            throw (IllegalStateException) error;
        }
        throw new IllegalStateException(message, error);
    }

    @Override
    public boolean reportErrors() {
        return fReportErrors;
    }

    public void setReportErrors(boolean reportErrors) {
        fReportErrors = reportErrors;
    }

}