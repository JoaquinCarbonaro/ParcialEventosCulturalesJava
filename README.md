# 🎭 Sistema de Gestión de Eventos Culturales

Este proyecto implementa un sistema para la gestión de eventos culturales utilizando **Java** y **NetBeans**, con persistencia de datos mediante **serialización**. El objetivo es aplicar los conceptos fundamentales de **Programación Orientada a Objetos (POO)**, como herencia, polimorfismo, manejo de archivos y excepciones.

---

## 🛠️ Funcionalidades

- **CRUD Completo**: Permite crear, leer, actualizar y eliminar eventos culturales.
- **Persistencia de Datos**: Guarda la información en archivos binarios usando serialización.
- **Jerarquía de Eventos**: Uso de clases abstractas y herencia para representar diferentes tipos de eventos:
  - `Evento` (abstracta)
  - `Concierto`
  - `Conferencia`
- **Interfaz Funcional**: Permite filtrar eventos según fecha, tipo o capacidad restante usando expresiones lambda.
- **Estadísticas y Exportación**:
  - Generación de estadísticas en **formato texto** o **JSON**
  - Exportación de eventos a **archivo CSV**
- **(Bonus)** Interfaz gráfica usando JavaFX

---

## 📁 Estructura del Proyecto

El código está organizado en los siguientes paquetes:

src/

├── com.cultura.eventos → Clases base y derivadas de eventos (como Evento, Concierto, etc.)

├── com.cultura.gestores → Clases encargadas de gestionar los eventos (alta, baja, filtros, etc.)

└── com.cultura.excepciones → Excepciones personalizadas para manejar errores específicos

---

## ✅ Checklist de Conceptos Aplicados

- [x] **Clases y Objetos**
- [x] **Atributos y Métodos** (públicos, privados, protegidos)
- [x] **Constructores y Sobrecarga**
- [x] **Encapsulamiento** (getters/setters con validaciones)
- [x] **Herencia y Polimorfismo**
- [x] **Abstracción** (clases y métodos abstractos)
- [x] **Interfaces y Lambdas**
- [x] **Excepciones** (try-catch, excepciones personalizadas)
- [x] **Colecciones** (`ArrayList`, `HashMap`)
- [x] **Generics** (`List<T>`, clases genéricas)
- [x] **Serialización** (`ObjectOutputStream`, `ObjectInputStream`)
- [x] **Manejo de Archivos** (`BufferedReader`, `BufferedWriter`)
- [x] **Conversión a JSON** (con `Gson`)
- [x] **JavaFX**

---

## ▶️ Cómo ejecutar el proyecto

1. Cloná este repositorio en tu máquina local.
2. Abrilo con **NetBeans**.
3. Ejecutá la clase `Main.java`.
4. Usá el menú en consola para gestionar los eventos.

---

## 📸 UML

El modelado UML del sistema se encuentra incluido como PDF en el repositorio.

---

## 👤 Autor

**Joaquin Carbonaro**  
📚 Universidad Tecnológica Nacional (UTN)  
🧠 Materia: Programación 2

---

## 🧾 Licencia

Este proyecto se comparte con fines educativos. Libre de usar, modificar y mejorar, siempre que se reconozca el autor original.

