import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/common/Header";
import Home from "./pages/Home";
import Footer from "./components/common/Footer";

function App() {
  return (
    <BrowserRouter>
      <Header />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </main>
      <Footer />
    </BrowserRouter>
  );
}

export default App;