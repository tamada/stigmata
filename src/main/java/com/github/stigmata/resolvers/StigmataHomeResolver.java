package com.github.stigmata.resolvers;


interface StigmataHomeResolver{
    public boolean isTarget(String osName);

    public String getStigmataHome();
}
