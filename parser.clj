;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.


(ns example.core
  (:require [instaparse.core :as insta]))

(def parser
  (insta/parser
   "expr = number | operation | assignement
    assignement =  varName space ':=' space elt
    operation = <'('>? (elt) space operator space (elt) <')'>?
    <elt> = number | operation
    operator = '=' / ('*' | '/' |'%') / ('+' | '-')
    <space> = <#'[ ]+'>
    letter = #'[a-zA-Z]'
    varName = #'[a-zA-Z]+'
    number = #'[0-9]+(.[0-9]+)?'"))

(parser "a := 12 * 3")

;choix de l'opérateur
(defn choose-operator [op]
  (case op
    "+" +
    "-" -
    "*" *
    "/" /
    "%" mod
      ))

;assignement d'une valeur a une variable
(defn assign [variable value env]
  (merge  {variable value} env)
  )

;logique de parsing
(defn transform-options [env]
  {:number read-string
   :varName read-string
   :operator choose-operator
   :operation #(%2 %1 %3)
   :assignement #(assign %1 %3 env)
   :expr identity})

(defn parse [input env]
  (->> (parser input) (insta/transform (transform-options env))))

(defn simple-parse [input]
  parse [input {}])

;boucle repl
(defn repl [expression]
  (let [env {}]
    (loop [expr (clojure.string/split  expression #"@" ) ;on coupe sur le retour a la ligne (il faut bien un caractere de démarcation)
           env env]
      (if-not (empty? expr)  (recur (rest expr) (print (parse  (first expr) env) "\n")))

    )))

(parse "a := 3 + 2" {})
(repl "a := 3 + 3@b := 5 + 7"); la sortie est dans la console, la méthode ne retourne rien





