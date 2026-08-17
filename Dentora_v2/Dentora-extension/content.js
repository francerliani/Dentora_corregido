// 🎤 Crear botón flotante
const button = document.createElement("button");
button.innerText = "🎤";
button.id = "dentora-mic";

button.style.position = "fixed";
button.style.bottom = "20px";
button.style.right = "20px";
button.style.zIndex = "9999";
button.style.padding = "15px";

document.body.appendChild(button);

// 🟡 Panel visual
let panel = document.createElement("div");

panel.id = "dentora-panel";
panel.style.position = "fixed";
panel.style.bottom = "80px";
panel.style.right = "20px";
panel.style.background = "white";
panel.style.padding = "12px";
panel.style.border = "2px solid #007bff";
panel.style.borderRadius = "8px";
panel.style.zIndex = "9999";
panel.style.fontSize = "13px";
panel.style.minWidth = "220px";
panel.style.boxShadow = "0 2px 8px rgba(0,0,0,0.2)";
panel.style.display = "none";

document.body.appendChild(panel);

// 🎙️ Reconocimiento de voz
const recognition = new webkitSpeechRecognition();

recognition.lang = "es-AR";
recognition.continuous = false;
recognition.interimResults = false;

// ▶️ Click micrófono
button.addEventListener("click", () => {
    recognition.start();
    button.innerText = "🎙️ Escuchando...";
    panel.style.display = "block";
    panel.innerHTML = "<i>Escuchando...</i>";
});

// 🧠 Resultado de voz
recognition.onresult = async (event) => {

    const texto = event.results[0][0].transcript;

    console.log("Texto detectado:", texto);

    button.innerText = "🎤";
    panel.innerHTML = `<i>Procesando: "${texto}"...</i>`;

    try {

        const response = await fetch("http://localhost:9090/procesar-texto", {
            method: "POST",
            headers: { "Content-Type": "text/plain" },
            body: texto
        });

        const result = await response.text();
        console.log("Respuesta backend:", result);

        const data = JSON.parse(result);

        // ✅ BUGS CORREGIDOS: .join() en todos los arrays
        const piezasStr      = data.piezas.length      > 0 ? data.piezas.join(", ")      : "—";
        const superficiesStr = data.superficies.length > 0 ? data.superficies.join(", ") : "—";
        const patologiasStr  = data.patologias.length  > 0 ? data.patologias.join(", ")  : "—";

        // ✅ esPrevio: rojo = previo, azul = actual
        const color = data.esPrevio ? "🔴 Previo (rojo)" : "🔵 Actual (azul)";

        panel.innerHTML = `
            <div style="font-weight:bold; margin-bottom:6px; color:#007bff;">🦷 Dentora</div>
            <div><b>Piezas:</b> ${piezasStr}</div>
            <div><b>Superficies:</b> ${superficiesStr}</div>
            <div><b>Patologías:</b> ${patologiasStr}</div>
            <div><b>Estado:</b> ${data.estado}</div>
            <div><b>Tipo:</b> ${color}</div>
            <div style="margin-top:6px; font-size:11px; color:#888;">"${texto}"</div>
        `;

        // ✍️ Escribir en input si existe
        const input = document.querySelector("input");
        if (input) {
            // ✅ BUG CORREGIDO: era data.patologias sin .join()
            input.value = `Piezas: ${piezasStr} - ${patologiasStr}`;
        }

    } catch (error) {
        console.error("Error:", error);
        panel.innerHTML = "❌ Error conectando con el backend.<br><small>¿Está corriendo en localhost:9090?</small>";
    }
};

// ❌ Error de reconocimiento
recognition.onerror = (event) => {
    button.innerText = "🎤";
    panel.style.display = "block";
    panel.innerHTML = `❌ Error de micrófono: ${event.error}`;
};
