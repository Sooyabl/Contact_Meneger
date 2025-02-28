document.addEventListener("DOMContentLoaded", function () {
    document.querySelector("#contactForm").addEventListener("submit", async function (event) {
        event.preventDefault(); // Запобігаємо перезавантаженню сторінки

        const name = document.querySelector("#name").value;
        const phone = document.querySelector("#phone").value;
        const email = document.querySelector("#email").value;

        if (!name || !phone || !email) {
            alert("Будь ласка, заповніть всі поля!");
            return;
        }

        const contact = { name, phone, email };

        try {
            const response = await fetch("http://localhost:8080/api/contacts", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(contact)
            });

            if (response.ok) {
                alert("Контакт додано!");
                document.querySelector("#contactForm").reset(); // Очищаємо форму
                fetchContacts(); // Оновлюємо список контактів
            } else {
                alert("Помилка при збереженні контакту!");
            }
        } catch (error) {
            console.error("Помилка:", error);
            alert("Не вдалося з'єднатися з сервером.");
        }
    });

    fetchContacts(); // Завантажуємо контакти при старті
});


async function fetchContacts() {
    const response = await fetch("http://localhost:8080/api/contacts");
    const contacts = await response.json();
    const tableBody = document.querySelector("#contactsTable tbody");
    tableBody.innerHTML = "";

    contacts.forEach(contact => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${contact.name}</td>
            <td>${contact.phone}</td>
            <td>${contact.email}</td>
            <td>
                <button onclick="editContact(${contact.id})">✏️</button>
                <button onclick="deleteContact(${contact.id})">🗑️</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

async function deleteContact(id) {
    if (!confirm("Ви впевнені, що хочете видалити контакт?")) return;

    const response = await fetch(`http://localhost:8080/api/contacts/${id}`, {
        method: "DELETE"
    });

    if (response.ok) {
        fetchContacts(); // Оновлюємо список після видалення
    } else {
        alert("Не вдалося видалити контакт");
    }
}

async function editContact(id) {
    const name = prompt("Введіть нове ім'я:");
    const phone = prompt("Введіть новий телефон:");
    const email = prompt("Введіть новий email:");

    if (!name || !phone || !email) {
        alert("Усі поля повинні бути заповнені!");
        return;
    }

    const updatedContact = { name, phone, email };

    try {
        const response = await fetch(`http://localhost:8080/api/contacts/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedContact)
        });

        if (response.ok) {
            alert("Контакт оновлено!");
            fetchContacts(); // Оновлюємо список контактів
        } else {
            alert("Помилка при оновленні контакту!");
        }
    } catch (error) {
        console.error("Помилка:", error);
        alert("Не вдалося з'єднатися з сервером.");
    }
}
