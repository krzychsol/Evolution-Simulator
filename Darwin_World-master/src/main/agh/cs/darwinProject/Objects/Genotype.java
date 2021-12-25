package agh.cs.darwinProject.Objects;

import java.util.Arrays;
import java.util.Random;

public class Genotype {

    private final int[] genesCount = {0, 0, 0, 0, 0, 0, 0, 0};
    private final static Random generator = new Random();

    public Genotype() {
        for (int i = 0; i < 32; i++) {
            genesCount[generator.nextInt(8)] += 1;
        }
        correctGenes();
    }

    // creates genotype based on parents genotype

    public Genotype(Genotype stronger, Genotype weaker) {

        int startOfNewGroup1 = generator.nextInt(31) + 1; // [1, 31]
        int startOfNewGroup2 = generator.nextInt(31) + 1; // [1, 31]
        while (startOfNewGroup1 == startOfNewGroup2)
            startOfNewGroup2 = generator.nextInt(31);

        // start of first group is always 0.
        int startOfSecondGroup = Math.min(startOfNewGroup1, startOfNewGroup2);
        int startOfThirdGroup = Math.max(startOfNewGroup1, startOfNewGroup2);

        copyGeneGroup(stronger, this, 0, startOfSecondGroup);
        copyGeneGroup(weaker, this, startOfSecondGroup, startOfThirdGroup);
        copyGeneGroup(stronger, this, startOfThirdGroup, 32);
        correctGenes();
    }


    public int getRandomGene(){

        int n = generator.nextInt(32); // returns n-th gene in sorted array of genes
        int currGeneType = 0;
        int i = 0;

        while (i <= n){
            i += genesCount[currGeneType];
            currGeneType +=1;
        }
       // return result -1 as it is always incremented at the end of the loop
        return currGeneType-1;

    }

    private void correctGenes() {

        int dupGenes = 0; // number of genes that may be replaced
        for (int i = 0; i < 8; i++) {
            if (genesCount[i] > 0)
                dupGenes +=  genesCount[i] - 1;
        }

        for (int geneID = 0; geneID < 8; geneID++) {

            if (genesCount[geneID] == 0) {
                int n = generator.nextInt(dupGenes);
                // iterate over "virtual" genes to find n-th frequent gene
                for (int j = 0; j < 8; j++) {
                    if (genesCount[j] > 1) {
                        n -= (genesCount[j] - 1);
                        if (n <= 0) { // found gene to replace
                            genesCount[j] -= 1;
                            genesCount[geneID] += 1;
                            dupGenes -=1;
                            break;
                        }

                    }
                }
            }
        }
    }


    private void copyGeneGroup(Genotype Mother, Genotype child, int groupStart, int groupEnd) {
        int currGene = groupStart;
        int currGeneType = 0;
        // iterate over genes we do not copy
        while (currGene >= Mother.genesCount[currGeneType]){
            currGene-= Mother.genesCount[currGeneType];
            currGeneType += 1;
        }

        for (int i=groupStart; i< groupEnd; i++){
            currGene += 1;
            child.genesCount[currGeneType] += 1;
            if (Mother.genesCount[currGeneType] == currGene) {
                currGene = 0;
                currGeneType += 1;
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof Genotype))
            return false;

        Genotype that = (Genotype) other;
        return Arrays.equals(this.genesCount,that.genesCount);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genesCount);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i<8; i++){
            for (int j=0; j< genesCount[i]; j++){
                res.append(i);
                res.append(" ");
            }
        }
        return res.toString();
    }

}

