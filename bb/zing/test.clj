#!/home/robbie/bin/bb
(ns zing.test)

(def esc (char 27))
(def home (char 13))
(def cls (str esc "[2J"))
(def mtl (str esc "[;H")) ;move top left
(def cursor-off (str esc "[?25l"))
(def cursor-on (str esc "[?25h"))

(def green42 (str esc "[38;5;42m"))
(def green82 (str esc "[38;5;83m"))
(def resetcolor (str esc "[0m"))

(def hh "─") 
(def HH "━") 
(def lB "▌")
(def c0M "●")
;cursor position?
;(print (str esc "[6n"))


(defn pos [x y]
  (str esc "[" x ";" y "H"))
(defn expand-str [s]
  (loop [s s]
    (let [m (re-matcher #"\t+" s)
          tabs (re-find m)]         ; Like Perl's $&
      (if tabs
        (let [before-tabs (subs s 0 (. m (start)))  ; Like Perl's $`
              after-tabs (subs s (. m (end)))]      ; $'
          (recur (str before-tabs
                      (apply str (repeat (- (* (count tabs) 8)
                                            (mod (count before-tabs) 8))
                                         " "))
                      after-tabs)))
        s))))

(defn zingprint [^String s]
  (let [s (expand-str s)
        l (count s)
        pause 20;(/ 500 l)
        zstr (str hh c0M)
        zlen (count zstr)
        ]
    (doseq [x (range 0 (inc l))
            :let [sz (min zlen (Math/abs (- l x)))]] 
      (print resetcolor home)
      (print (subs s 0 x))
      (print (str green82  (subs zstr (- zlen sz) )) )
      (flush)
      (Thread/sleep pause))))

(defn zingprint-dots [^String s]
  (let [s (expand-str s)
        l (count s)
        pause 10;(/ 500 l)
        letters (vec s)
        letterseq (shuffle (range 1 (inc l)))
        ]
    (doseq [x (range 1 (inc l))
            :let [zstr (apply str (repeatedly l #(rand-nth ["∴" "∵" "∷" "∶"])))]]
      (print (str home green82 zstr))
      (print resetcolor home)
      (doseq [y (take x letterseq)
              :let [pos (nth letterseq (dec y))]]
        (print (str esc "[" pos "G" (nth letters (dec pos)))))
      (flush)
      (Thread/sleep pause))))

;(println "y")
;(println "z")
;(print cls mtl)
;(zingprint-dots "this is a test with a big long string")
;(println)
;(zingprint-dots "this is a test with a big long string even longer than before it's REALLY long wheeeee!")

(print cursor-off)
(doseq [line (line-seq (clojure.java.io/reader *in*))]
  (zingprint-dots line)
  (println)
  )
(print cursor-on)
