package br.ufmt.harmonizacao.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Set;

import br.ufmt.harmonizacao.reader.CSVReader;
import java.lang.invoke.MethodHandles;

public class Nomes {

    public Set<Nome> inventores;

    public Nomes() {
        inventores = new HashSet<Nome>();
    }

    public boolean addInventor(Nome i) {
        if (inventores.add(i)) {
            return true;
        }
        return false;
    }

    /**
     * Cria um Set de patentes a partir de um arquivo csv Não utulizei nenhum
     * filtro para saber se o arquivo é csv ou não
     *
     * @param caminho caminho para o arquivo
     * @return Seta de patentes do arquivo //
     */
//	public static Nomes criarPorArquivoCsv(String caminho) {
//		CSVReader arquivo = new CSVReader(caminho);
//		Nomes nomes = new Nomes();
//		try {
//			arquivo.openTextFile();
//			// Ler o cabeçalho
//			if (arquivo.next()) {
//				String linha = arquivo.readLine();
//			}
//			while (arquivo.next()) {
//
//				String linha = arquivo.readLine();
//				if (!linha.trim().isEmpty()) {
//					// Separando os dados de depositantes e aplicantes
//					String[] vDados = linha.split(",");
//					// Testa para ver se a patente tem depositantes
//					if (vDados.length >= 2) {
//						String[] inventores = vDados[0].replaceAll("\"", "")
//								.split(";");
//						for (String i : inventores) {
//							i = i.replace("]", "").replace("[", ":");
//							String[] inventor = i.split(":");
//							if (inventor.length > 1) {
//								nomes.addInventor(new Inventor(inventor[0]
//										.trim(), inventor[1].trim()));
//							} else {
//								nomes.addInventor(new Inventor(inventor[0]
//										.trim(), ""));
//							}
//						}
//
//						String[] depositantes = vDados[1].replace("\"", "")
//								.split(";");
//						for (String d : depositantes) {
//							d = d.replace("]", "").replace("[", ":");
//							String[] depositante = d.split(":");
//							if (depositante.length > 1) {
//								nomes.addDepositante(new Depositante(
//										depositante[0].trim(), depositante[1]
//												.trim()));
//							} else {
//								nomes.addDepositante(new Depositante(
//										depositante[0].trim(), ""));
//							}
//						}
//					} else if (vDados.length > 0 && vDados.length < 2
//							&& (vDados[0] == null || vDados[0].isEmpty())) {
//						// Temos apenas depositantes para patnete
//						String[] depositantes = vDados[1].replace("\"", "")
//								.split(";");
//						for (String d : depositantes) {
//							d = d.replace("]", "").replace("[", ":");
//							String[] depositante = d.split(":");
//							if (depositante.length > 1) {
//								nomes.addDepositante(new Depositante(
//										depositante[0].trim(), depositante[1]
//												.trim()));
//							} else {
//								nomes.addDepositante(new Depositante(
//										depositante[0].trim(), ""));
//							}
//						}
//					} else if (vDados.length > 0 && vDados.length < 2
//							&& (vDados[0] == null || vDados[0].isEmpty())) {
//						// Então temos apenas inventores nesta petente
//						String[] inventores = vDados[0].replace("\"", "")
//								.split(";");
//						for (String i : inventores) {
//							i = i.replace("]", "").replace("[", ":");
//							String[] inventor = i.split(":");
//							if (inventor.length > 1) {
//								nomes.addInventor(new Inventor(inventor[0]
//										.trim(), inventor[1].trim()));
//							} else {
//								nomes.addInventor(new Inventor(inventor[0]
//										.trim(), ""));
//							}
//						}
//					}
//				}
//			}
//			return nomes;
//		} catch (IOException e) {
//			System.out.println("ERRO: " + e);
//		}
//		return nomes;
//	}
    public static Nomes criarPorArquivoCsv(String caminho) {

        CSVReader arquivo = new CSVReader(caminho);
        Nomes nomes = new Nomes();
        try {
            arquivo.openTextFile();
            // Ler o cabeçalho
            if (arquivo.next()) {
                String linha = arquivo.readLine();
            }
            while (arquivo.next()) {
                Nome nome = new Nome();
                nome.setNome(arquivo.readLine());
                nomes.addInventor(nome);
            }
        }catch (Exception e) {
            
        }
        return nomes;
    }

	

    public void escrever() {
        System.out.println("Patente :\n Nomes");
        for (Nome i : inventores) {
            System.out.println(i.getNome());
        }

    }

    public Set<Nome> getInventores() {
        return inventores;
    }

    public void setInventores(Set<Nome> inventores) {
        this.inventores = inventores;
    }

}
