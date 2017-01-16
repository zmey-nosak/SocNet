<%@ page contentType="text/html;charset=UTF-8" language="java" %>

</html>
<head>
    <meta charset="utf-8">

    <title>Форма регистрации</title>
    <link rel="stylesheet" href="/css/registration.css">
</head>
<body>
<link rel="stylesheet" type="text/css" href="tcal.css">
<script src="/tcal.js"></script>
<p><i>${message}</i></p>

<form action="/register/" method="post">
    <input type="hidden" name="action" value="add">
    <section class="container one">
        <p><label for="i_name">Имя</label>
            <input type="text" name="i_name" pattern="^[А-Яа-яЁё\s]+$" value="${user.i_name}" required><span></span></p>

        <p><label for="f_name">Фамилия</label>
            <input type="text" name="f_name" pattern="^[А-Яа-яЁё\s]+$" value="${user.f_name}" required><span></span></p>

        <p><label for="dob">Дата рождения</label>
            <input name="dob" value="" pattern="\d{2}\.\d{2}\.\d{4}" required><span></span></p>

        <p><label for="email">Эл. почта</label>
            <input type="email" name="email" value="${user.email}" required><span></span></p>

        <p><label for="password">Пароль</label>
            <input type="password" name="password" required><span></span></p>

        <p><label for="password">Повторите</label>
            <input type="password" name="password" required><span></span></p>
        <p>
            <button type="submit">Зарегистрироваться</button>
        </p>
        <p>
            <button onclick="">Отмена</button>
        </p>
    </section>
</form>

</body>
</html>