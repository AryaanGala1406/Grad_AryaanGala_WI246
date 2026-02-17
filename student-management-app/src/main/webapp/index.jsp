<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Management</title>

    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #f4f6fb;
        }

        .wrapper {
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .main-container {
            width: 100%;
            max-width: 420px;
        }

        .title {
            text-align: center;
            margin-bottom: 15px;
        }

        .title h1 {
            margin: 0;
            font-size: 24px;
        }

        .title h2 {
            margin: 5px 0 0;
            font-size: 14px;
            font-weight: normal;
            color: #555;
        }

        .container {
            background: white;
            padding: 25px 30px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
        }

        h3 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            margin-bottom: 14px;
        }

        label {
            font-size: 14px;
            margin-bottom: 6px;
            color: #444;
        }

        input {
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 14px;
            outline: none;
        }

        input:focus {
            border-color: #667eea;
            box-shadow: 0 0 5px rgba(102, 126, 234, 0.4);
        }

        .btn-group {
            display: flex;
            gap: 12px;
            margin-top: 18px;
        }

        button {
            flex: 1;
            padding: 10px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn-submit {
            background: #667eea;
            color: white;
        }

        .btn-submit:hover {
            background: #5a6fd6;
        }

        .btn-reset {
            background: #f44336;
            color: white;
        }

        .btn-reset:hover {
            background: #d7372c;
        }
    </style>
</head>

<body>

    <div class="wrapper">

        <div class="main-container">

            <div class="title">
                <h1>Student Management Application</h1>
                <h2>Add New Student</h2>
            </div>

            <div class="container">
                <h3>Student Entry Form</h3>

                <form method="post" action="addStudent">

                    <div class="form-group">
                        <label>Roll Number</label>
                        <input type="number" name="rollNo" required>
                    </div>

                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" required>
                    </div>

                    <div class="form-group">
                        <label>Standard</label>
                        <input type="number" name="standard" required>
                    </div>

                    <div class="form-group">
                        <label>School</label>
                        <input type="text" name="school" required>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn-submit">INSERT</button>
                        <button type="reset" class="btn-reset">RESET</button>
                    </div>

                </form>
            </div>

        </div>

    </div>

</body>

</html>
