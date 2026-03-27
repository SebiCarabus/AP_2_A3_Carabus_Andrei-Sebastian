<!DOCTYPE html>
<html>
<head>
    <title>Movie Catalog Report</title>
    <style>
        body { font-family: sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>Movie Catalog</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Genre</th>
                <th>Release Date</th>
                <th>Duration (min)</th>
                <th>Score</th>
            </tr>
        </thead>
        <tbody>
            <#list movies as movie>
            <tr>
                <td>${movie.id}</td>
                <td>${movie.title}</td>
                <td>${movie.genreName!" "}</td>
                <td>${movie.releaseDate!" "}</td>
                <td>${movie.duration}</td>
                <td>${movie.score}</td>
            </tr>
            </#list>
        </tbody>
    </table>
</body>
</html>