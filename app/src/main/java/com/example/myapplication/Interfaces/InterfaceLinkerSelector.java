package com.example.myapplication.Interfaces;

public class InterfaceLinkerSelector {
    public InterfaceLinker m_linkerA;
    public InterfaceLinker m_linkerB;
    public InterfaceLinker m_linkerC;
    public InterfaceLinker m_linker;

    public InterfaceLinkerSelector(){
        m_linkerA = new InterfaceLinker();
        m_linkerB = new InterfaceLinker();
        m_linkerC = new InterfaceLinker();
        m_linker = m_linkerA;
    }

    public void selectLinker(int linkerNumber)
    {
        switch(linkerNumber){
            case 0:
                m_linker = m_linkerA;
                break;
            case 1:
                m_linker =   m_linkerB;
                break;
            case 2:
                m_linker = m_linkerC;
                break;
            default:
                m_linker = m_linkerA;
                break;
        }
    }
}
