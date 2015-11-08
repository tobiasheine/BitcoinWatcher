package eu.tobiasheine.bitcoinwatcher.di;

class InjectedComponent <T> {

    private final T implementation;

    public InjectedComponent(T implementation) {
        this.implementation = implementation;
    }

    public T getImplementation(){
        return implementation;
    }
}
