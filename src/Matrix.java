public class Matrix {
    //TODO: Clean code
    //TODO: Would for each loops be neater implementation?
    //TODO: Implement matrix subtraction?
    //TODO: Check if I need a new implementation for vectors
    //TODO: Ensure that Matrix class doesn't create vectors
    double[][] matrix;
    int rank;

    Matrix(double[][] matrix){//NEED TO DO
        this.matrix = matrix;
    }
    Matrix(int rows, int columns){
        this.matrix = new double[rows][columns];
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for (double[] doubles : this.matrix) {
            for (double aDouble : doubles) {output.append(aDouble).append("    ");}
            output.append("\n");
        }
        return output.toString();
    }

    int getNumRows(){
        return this.matrix.length;
    }
    int getNumColumns(){
        return this.matrix[0].length;
    }
    double getElement(int row, int column){
        return this.matrix[row][column];
    }
    void setElement(int row, int column, double value){
        this.matrix[row][column] = value;
    }

    //TODO: Why is deleted Row always zero?
    Matrix subMatrix(int deletedRow, int deletedColumn){
        Matrix temp = new Matrix(this.matrix.length - 1, this.matrix[0]. length - 1);
        int k = 0, l = 0;
        for (int i = 0; i < this.matrix.length; i++){
            for (int j = 0; j < this.matrix[0].length; j++){
                if (!(i == deletedRow || j == deletedColumn)){
                    temp.matrix[k][l] = this.matrix[i][j];
                    l++;
                }
                if (i == deletedRow){k--;break;}
            }
            k++;
            l = 0;
        }
        return temp;
    }

    //TODO: Check
    Matrix transpose(){
        Matrix transposedMatrix = new Matrix(this.getNumColumns(), this.getNumRows());
        for(int a = 0; a < transposedMatrix.getNumRows(); a++){
            for(int b = 0; b < transposedMatrix.getNumColumns(); b++){
                transposedMatrix.setElement(a, b, this.getElement(b, a));
            }
        }
        return transposedMatrix;
    }

    /*
    Find determinant of matrix passed in as a parameter.

    @param  a Matrix object of which the determinant will be taken.
    @return   Scalar value representing the determinant of Matrix a.
     */
    //TODO: Check that refactored code works.
    //TODO: Make it instance.
    static double det(Matrix a){
        if (a.matrix.length == a.matrix[0].length){
            double determinant = 0;

            if (a.matrix.length == 2){
                determinant = (a.matrix[0][0] * a.matrix[1][1]) - (a.matrix[0][1] * a.matrix[1][0]);

                return determinant;
            }else{
                for (int j = 0; j < a.matrix.length; j++) {
                    determinant += Math.pow(-1.0, j) * a.matrix[0][j] * det(a.subMatrix(0, j));
                }

                return determinant;
            }

        }else{
            System.out.println("You cannot calculate the determinant of this matrix");
            return 0.0;
        }
    }

    static Matrix add(Matrix a, Matrix b) {
        if ((a.matrix.length == b.matrix.length) && (a.matrix[0].length == b.matrix[0].length)){
            Matrix temp = new Matrix(a.matrix.length, a.matrix[0].length);
            for (int i = 0; i < a.matrix.length; i++){
                for (int j = 0; j < a.matrix[0].length; j++){
                    temp.matrix[i][j] = a.matrix[i][j] + b.matrix[i][j];
                }
            }
            return temp;
        } else{
            System.out.println("You can't add these matrices");
            return null;
        }
    }

    /*
    Finds the dot product between two matrices passed in as parameters.

    @param  a Matrix object that goes first in the dot product.
    @param  b Matrix Object that goes second in the dot product.
    @return   Scalar value representing the dot product between matrix a and Matrix b.
     */
    static double dotProduct(Matrix a, Matrix b){
        double product = 0;

        if ((a.matrix.length == b.matrix.length) && (a.matrix[0].length == b.matrix[0].length)) {
            for (int i = 0; i < a.matrix.length; i++){
                for (int j = 0; j < a.matrix[0].length; j++){
                    product += a.matrix[i][j] * b.matrix[i][j];
                }
            }

            return product;
        }else{
            System.out.println("You cannot take the dot product of these two matrices");

            return 0.0;
        }
    }

    /*
    Finds the cross product between two matrices passed in as parameters.

    @param  a Matrix object that goes first in the cross product.
    @param  b Matrix object that goes second in the cross product.
    @return   Matrix object result of the cross product between Matrix a and Matrix b.
     */
    static Matrix crossProduct(Matrix a, Matrix b){
        Matrix temp = new Matrix(a.matrix.length, b.matrix[0].length);

        int tempPointInArray;

        for (int i = 0; i < temp.matrix.length; i++){
            for (int j = 0; j < temp.matrix[0].length; j++){
                tempPointInArray = 0;

                for (int k = 0; k < temp.matrix.length; k++){
                    tempPointInArray += a.matrix[i][k] * b.matrix[k][j];
                }

                temp.matrix[i][j] = tempPointInArray;
            }
        }

        return temp;
    }

    //TODO: Redo once vector matrix multiplication is implemented
    //TODO: Check if it works
    static Matrix multiplication(Matrix matrix1, Matrix matrix2){
        if(!(matrix1.getNumColumns() == matrix2.getNumRows())){
            System.out.println("Matrix 1 and Matrix 2 are not compatible.");
            return null;
        }
        Matrix product = new Matrix(matrix1.getNumRows(), matrix2.getNumColumns());
        for(int a = 0; a < product.getNumRows(); a++){
            for(int b = 0; b < product.getNumColumns(); b++){
                double sum = 0;
                for(int c = 0; c < matrix1.getNumColumns() ; c++){
                    sum += matrix2.getElement(c, b) * matrix1.getElement(a, c);
                }
                product.setElement(a, b, sum);
            }
        }
        return product;
    }

    //TODO: Check if the method works.
    //TODO: Rewrite code based on own algo or making this more readable/ understandable.
    Matrix reducedRowEchelon(){
        Matrix a = this;
        int lead = 0;
        int rowCount = this.getNumRows();
        int colCount = this.getNumColumns();
        int b;
        double[] temp;
        for (int c = 0; c < rowCount; c++){
            if(colCount <= lead){return a;}
            b = c;
            while( a.matrix[b][lead] == 0){
                b++;
                if(rowCount == b){
                    b = c;
                    lead++;
                    if(colCount == lead){return a;}
                }
            }
            temp = a.matrix[b];
            a.matrix[b] = a.matrix[c];
            a.matrix[c] = temp;
            if(a.matrix[c][lead] != 0){
                //Check if b is the right index to be used here.
                for(int d = 0; d < colCount; d++){a.matrix[c][d] /= a.matrix[c][lead];}
            }
            for(int e = 0; e < rowCount; e++){
                if(e != c){
                    for(int f = 0; f < colCount; f++){a.matrix[e][f] -= a.matrix[e][lead] * a.matrix[c][f];}
                }
            }
            lead++;
        }
        return a;
    }

    static Matrix scalarMultiplication(Matrix matrix, double scalar){
        Matrix scaledMatrix = new Matrix(matrix.getNumRows(), matrix.getNumColumns());
        for(int a = 0; a < matrix.getNumRows(); a++){
            for(int b = 0; b < matrix.getNumColumns(); b++){
                scaledMatrix.setElement(a, b, scalar * matrix.getElement(a, b));
            }
        }
        return scaledMatrix;
    }
}

